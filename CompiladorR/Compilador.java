package CompiladorR;

public class Compilador {

    public static void main(String[] args) {
        Lexico lexico = new Lexico("/workspace/CompiladorR/CompiladorR/Codigo.txt");
        Semantico sintatico = new Semantico(lexico);
        
        sintatico.programa();
        System.out.println("Deu tudo certo!!");
    }
}
