package Test;

public class FinalTest {

    public static String getString(){
        String str = "Abdullah";
        return str;
    }

    public static int getInt(){
        int num = 1;
        return num;
    }

    public static void main(String[] args){
        int var1 = 5;
        int var2 = 6;
        int var3 = var1 + var2;

        while(var2 < 10){
            var2 = var2+2;
        }

        var3 = getInt();

        if (var3 == 1) {
            String name = "Maaz";
            name = getString();
        }

    }
}
