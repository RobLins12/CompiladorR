package CompiladorR;

public class Compilador {

    public static void main(String[] args) {
        Lexico lexico = new Lexico("/workspace/CompiladorR/CompiladorR/Codigo.txt");
        Sintatico sintatico = new Sintatico(lexico);
        
        sintatico.programa();
        Token t = null;
        while((t = lexico.nextToken()) != null){  
            System.out.println(t.toString());
        }
        
    }
}
