package Test;

import org.eclipse.jdt.core.dom.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


interface boo{
    void setInteger(int value);
    int getInteger();
}
public class HelloWorld {

    public static void main(String[] args) throws IOException {
        // Prints "Hello, World" to the terminal window.

        FirstClass myClass = new FirstClass(3,"Abdullah");

        //LogGod TempClass = new LogGod("name");

        int x = 5;
        myClass.setInteger(x);
        int y = 4;
        int z=3;
        z=6;
        y=myClass.getInteger();

        int xx = 0;
        while(xx == 0){
            int ds = 2;
            xx = 1;
        }

        for(int i =0; i<3; i++){
            z = z+1;
        }

        String newstr ="";
        if (y ==x) {
            String f = "Maaz";
            myClass.setString(f);
            newstr = myClass.getString();
        }
        else {
            String f = "We screwed";
            myClass.setString(f);
            newstr = myClass.getString();
        }
        System.out.println(newstr);


    }

    public static class FirstClass implements boo{
        private int var1 = 0;
        private String str1 = " ";
        public String getString(){
            return str1;
        }

        public FirstClass(int value, String str){
            this.var1 = value;
            this.str1 = str;
        }


        @Override
        public void setInteger(int value) {
            this.var1 = value;
        }

        public int getInteger(){
            return var1;
        }

        public void setString(String value){
            this.str1 = value;
        }


    }

}