import java.util

import org.eclipse.jdt.core.dom.{ASTNode, ASTVisitor, AnonymousClassDeclaration, ArrayAccess, Assignment, Block, BlockComment, ClassInstanceCreation, CompilationUnit, ConditionalExpression, ConstructorInvocation, CreationReference, Expression, ExpressionStatement, FieldDeclaration, ForStatement, IMethodBinding, IfStatement, InfixExpression, Initializer, MethodDeclaration, MethodInvocation, MethodRefParameter, Name, NumberLiteral, ReturnStatement, SimpleName, SimpleType, Statement, StringLiteral, Type, TypeDeclaration, TypeDeclarationStatement, VariableDeclarationExpression, VariableDeclarationFragment, VariableDeclarationStatement, WhileStatement}
import org.eclipse.jdt.internal.compiler.ast.IntLiteral
import org.eclipse.jface.text.Document

class Visitor(cu: CompilationUnit) extends ASTVisitor {

  val names = new util.HashSet[String]()
  val ast = cu.getAST
  var ifNamer = 0


  override def endVisit(node: VariableDeclarationStatement)= {

    val types:Type = node.getType
    val hbu:util.List[_] = node.fragments()
    val frag = hbu.get(0).asInstanceOf[VariableDeclarationFragment]
    val name = frag.getName
    val bind:AnyRef = frag.getInitializer.resolveConstantExpressionValue()
    val value:Expression = frag.getInitializer()
    val line:Int = node.getRoot.asInstanceOf[CompilationUnit].getLineNumber(node.getStartPosition)

    // creating a new method
    val instrumInvocation = ast.newMethodInvocation
    instrumInvocation.setExpression(ast.newSimpleName("LogAssistant"))
    instrumInvocation.setName(ast.newSimpleName("instrum"))
    val instrumArguments = instrumInvocation.arguments().asInstanceOf[util.List[Expression]]

    // adding line number
    val lineNumber:NumberLiteral = ast.newNumberLiteral()
    lineNumber.setToken(line.toString)
    instrumArguments.add(lineNumber)

    // adding action statement
    val action:StringLiteral = ast.newStringLiteral()
    action.setLiteralValue("Declaration")
    instrumArguments.add(action)

    // adding action statement
    val name1:StringLiteral = ast.newStringLiteral()
    name1.setLiteralValue(name.getIdentifier)
    instrumArguments.add(name1)

    // adding name of variable
    val value1 = ast.newSimpleName(name.getIdentifier)
    instrumArguments.add(value1)

    // final statement
    val finalStatment = ast.newExpressionStatement(instrumInvocation)

    // adding final statement in the AST
    node.getParent match {
      case body: Block =>
        val indexToInsert = body.statements().asInstanceOf[util.List[ASTNode]].indexOf(node)
        body.statements().asInstanceOf[util.List[Statement]].add(indexToInsert+1, finalStatment)
    }

    //LogGod.instrum("Declaration",name,value,bind,types,line)
  }


  override def endVisit(node: Assignment) = {
    val lhs = node.getLeftHandSide
    val rhs = node.getRightHandSide
    val bind:AnyRef = rhs.resolveConstantExpressionValue()
    val line = node.getRoot.asInstanceOf[CompilationUnit].getLineNumber(node.getStartPosition)

    // creating a new method
    val instrumInvocation = ast.newMethodInvocation
    instrumInvocation.setExpression(ast.newSimpleName("LogAssistant"))
    instrumInvocation.setName(ast.newSimpleName("instrum"))
    val instrumArguments = instrumInvocation.arguments().asInstanceOf[util.List[Expression]]

    // adding line number
    val lineNumber:NumberLiteral = ast.newNumberLiteral()
    lineNumber.setToken(line.toString)
    instrumArguments.add(lineNumber)

    // adding action statement
    val action:StringLiteral = ast.newStringLiteral()
    action.setLiteralValue("Assignment")
    instrumArguments.add(action)

    // getting lhs as a simple name
    val lhs1 = node.getLeftHandSide.asInstanceOf[SimpleName]

    // adding lhs name
    val lhsName:StringLiteral = ast.newStringLiteral()
    lhsName.setLiteralValue(lhs1.getIdentifier)
    instrumArguments.add(lhsName)

    // adding lhs value
    val lhsValue = ast.newSimpleName(lhs1.getIdentifier)
    instrumArguments.add(lhsValue)


    // adding rhs string
    val rhs1 = node.getRightHandSide
    val rhsExpr:StringLiteral = ast.newStringLiteral()
    rhsExpr.setLiteralValue(rhs1.toString)
    instrumArguments.add(rhsExpr)

    // final statement
    val finalStatment = ast.newExpressionStatement(instrumInvocation)

    // adding final statement in the AST
    node.getParent.getParent match {
      case body: Block =>
        val indexToInsert = body.statements().asInstanceOf[util.List[ASTNode]].indexOf(node.getParent)
        body.statements().asInstanceOf[util.List[Statement]].add(indexToInsert+1, finalStatment)
    }
  }


  override def endVisit(node: IfStatement) = {
    val exp: Expression = node.getExpression
    val elseExp: Statement = node.getElseStatement
    val body: Statement = node.getThenStatement
    val line: Int = node.getRoot.asInstanceOf[CompilationUnit].getLineNumber(node.getStartPosition)

    // parts of if statement
    val lhs = exp.asInstanceOf[InfixExpression].getLeftOperand
    val op = exp.asInstanceOf[InfixExpression].getOperator
    val rhs = exp.asInstanceOf[InfixExpression].getRightOperand

    /*
    // create infix for rhs of assigment
    val expr = ast.newInfixExpression()
    expr.setLeftOperand(lhs)
    expr.setOperator(op)
    expr.setRightOperand(rhs)

    // create variable declaration fragment
    val expAlias = ast.newVariableDeclarationFragment()
    expAlias.setName(ast.newSimpleName("if" + ifNamer))
    expAlias.setInitializer(exp)

    // create variable declaration statement
    val aliasStatement = ast.newVariableDeclarationStatement(expAlias)

    // add node to AST
    node.getThenStatement.asInstanceOf[Block].statements().asInstanceOf[util.List[Statement]].add(0, aliasStatement)

     */

    // creating a new method
    val instrumInvocation = ast.newMethodInvocation
    instrumInvocation.setExpression(ast.newSimpleName("LogAssistant"))
    instrumInvocation.setName(ast.newSimpleName("instrum"))
    val instrumArguments = instrumInvocation.arguments().asInstanceOf[util.List[Expression]]

    // adding line number
    val lineNumber: NumberLiteral = ast.newNumberLiteral()
    lineNumber.setToken(line.toString)
    instrumArguments.add(lineNumber)

    // adding action statement
    val action = ast.newStringLiteral()
    action.setLiteralValue("IfStatement")
    instrumArguments.add(action)

    // adding expression
    val lhsName = ast.newStringLiteral()
    lhsName.setLiteralValue(exp.toString)
    instrumArguments.add(lhsName)

    // add method to AST
    val newStatement = ast.newExpressionStatement(instrumInvocation)
    node.getThenStatement.asInstanceOf[Block].statements().asInstanceOf[util.List[Statement]].add(0, newStatement)

  }


  override def endVisit(node: WhileStatement) = {
    val body:Statement = node.getBody
    val evalExp:Expression = node.getExpression
    val line:Int = node.getRoot.asInstanceOf[CompilationUnit].getLineNumber(node.getStartPosition)

    // creating a new method
    val instrumInvocation = ast.newMethodInvocation
    instrumInvocation.setExpression(ast.newSimpleName("LogAssistant"))
    instrumInvocation.setName(ast.newSimpleName("instrum"))
    val instrumArguments = instrumInvocation.arguments().asInstanceOf[util.List[Expression]]

    // adding line number
    val lineNumber: NumberLiteral = ast.newNumberLiteral()
    lineNumber.setToken(line.toString)
    instrumArguments.add(lineNumber)

    // adding action statement
    val action = ast.newStringLiteral()
    action.setLiteralValue("WhileStatement")
    instrumArguments.add(action)

    // adding expression
    val lhsName = ast.newStringLiteral()
    lhsName.setLiteralValue(evalExp.toString)
    instrumArguments.add(lhsName)

    // add method to AST
    val newStatement = ast.newExpressionStatement(instrumInvocation)
    node.getBody.asInstanceOf[Block].statements().asInstanceOf[util.List[Statement]].add(0, newStatement)

  }


  override def endVisit(node: MethodDeclaration) = {

    val name = node.getName
    val params = node.parameters().asInstanceOf[util.List[Expression]]
    val line:Int = node.getRoot.asInstanceOf[CompilationUnit].getLineNumber(node.getStartPosition)

    // creating a new method
    val instrumInvocation = ast.newMethodInvocation
    instrumInvocation.setExpression(ast.newSimpleName("LogAssistant"))
    instrumInvocation.setName(ast.newSimpleName("instrum"))
    val instrumArguments = instrumInvocation.arguments().asInstanceOf[util.List[Expression]]

    // adding line number
    val lineNumber:NumberLiteral = ast.newNumberLiteral()
    lineNumber.setToken(line.toString)
    instrumArguments.add(lineNumber)

    // adding action statement
    val action:StringLiteral = ast.newStringLiteral()
    action.setLiteralValue("MethodDeclaration")
    instrumArguments.add(action)

    // adding method name statement
    val methodName = ast.newStringLiteral()
    methodName.setLiteralValue(name.getIdentifier)
    instrumArguments.add(methodName)

    // adding method return type
    val methodType = ast.newStringLiteral()
    methodType.setLiteralValue(node.getReturnType2.toString)
    instrumArguments.add(methodType)

    // adding method arguments
    val methodParams = ast.newStringLiteral()
    methodParams.setLiteralValue(params.toString)
    instrumArguments.add(methodParams)

    // add method to AST
    val newStatement = ast.newExpressionStatement(instrumInvocation)
    node.getBody.statements().asInstanceOf[util.List[Statement]].add(0, newStatement)

  }


  override def endVisit(node: MethodInvocation) = {
    val args:util.List[_] = node.arguments()
    val method:SimpleName = node.getName
    val value:IMethodBinding = node.resolveMethodBinding()
    val line:Int = node.getRoot.asInstanceOf[CompilationUnit].getLineNumber(node.getStartPosition)

    //println(node.getName)
    //println(node.arguments())
    //println(node.getExpression)

  }

}
