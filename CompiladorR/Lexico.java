package CompiladorR;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Lexico {
    private char[] conteudo;
    private int indiceConteudo;
    
    public Lexico(String caminhoCodigoFonte){
        try {
            String conteudoStr;
            conteudoStr = new String(Files.readAllBytes(Paths.get(caminhoCodigoFonte)));
            this.conteudo = conteudoStr.toCharArray();
            this.indiceConteudo = 0;                        
        } catch (IOException ex) {
            ex.printStackTrace();
        }        
    }
    
    //Retorna próximo char
    private char nextChar(){
        return this.conteudo[this.indiceConteudo++];
    }
    
    //Verifica existe próximo char ou chegou ao final do código fonte
    private boolean hasNextChar(){
        return indiceConteudo < this.conteudo.length;
    }
    
    //Retrocede o índice que aponta para o "char da vez" em uma unidade
    private void back(){
        this.indiceConteudo--;
    }
    
    //Identificar se char é letra minúscula    
    private boolean isLetra(char c){
        return (c >= 'a') && (c <= 'z');
    }
    
    //Identificar se char é dígito
    private boolean isDigito(char c){
        return (c >= '0') && (c <= '9');
    }

    //Identificar se char é uma Aspa simples
    private boolean isAspas(char c){
        return (c == '\''); 
    }

    //Identificar se é palavra reservada
    private boolean isReservada(String s){
        return (s.equals("public"))  || (s.equals("private")) || (s.equals("protected"))
        || (s.equals("abstract"))  || (s.equals("class")) || (s.equals("extends"))
        || (s.equals("final"))  || (s.equals("implements")) || (s.equals("interface"))
        || (s.equals("native"))  || (s.equals("new")) || (s.equals("static"))
        || (s.equals("strictfp"))  || (s.equals("synchronized")) || (s.equals("transient"))
        || (s.equals("volatile"))  || (s.equals("break")) || (s.equals("case"))
        || (s.equals("continue"))  || (s.equals("default")) || (s.equals("do"))
        || (s.equals("else"))  || (s.equals("for")) || (s.equals("if"))
        || (s.equals("instanceof"))  || (s.equals("return")) || (s.equals("switch"))
        || (s.equals("while"))  || (s.equals("assert")) || (s.equals("catch"))
        || (s.equals("finally"))  || (s.equals("throw")) || (s.equals("throws"))
        || (s.equals("try"))  || (s.equals("import")) || (s.equals("package"))
        || (s.equals("boolean"))  || (s.equals("byte")) || (s.equals("char"))
        || (s.equals("double"))  || (s.equals("float")) || (s.equals("int"))
        || (s.equals("long"))  || (s.equals("short")) || (s.equals("super"))
        || (s.equals("this"))  || (s.equals("void")) || (s.equals("const"))
        || (s.equals("goto")); 
    }
    
    //Método retorna próximo token válido ou retorna mensagem de erro.
    public Token nextToken(){
        Token token = null;
        char c;
        int estado = 0;
        
        StringBuffer lexema = new StringBuffer();
        while(this.hasNextChar()){
            c = this.nextChar();            
            switch(estado){
                case 0:
                    if(c == ' ' || c == '\t' || c == '\n' || c == '\r' ){ //caracteres de espaço em branco ASCII tradicionais 
                        estado = 0;
                    }
                    else if(this.isDigito(c)){
                        lexema.append(c);
                        estado = 1;
                    }
                    else if(this.isLetra(c)){
                        lexema.append(c);
                        estado = 4;
                    }
                    else if(c == ')' || 
                            c == '(' ||
                            c == '{' ||
                            c == '}' ||
                            c == ',' ||
                            c == ';'){
                        lexema.append(c);
                        estado = 5;
                    }
                    else if(c == '+' || 
                            c == '-' ||
                            c == '*' ||
                            c == '/' ||
                            c == '%' ){
                        lexema.append(c);
                        estado = 6;
                    }
                    else if(c == '>'){
                        lexema.append(c);
                        estado = 7;
                    }
                    else if(c == '<'){
                        lexema.append(c);
                        estado = 9;
                    }
                    else if(c == '='){
                        lexema.append(c);
                        estado = 11;
                    }
                    else if(isAspas(c)){
                        lexema.append(c);
                        estado = 13;
                    }
                    else if(c == '$'){
                        lexema.append(c);
                        estado = 99;
                        this.back();
                    }else{
                        lexema.append(c);
                        throw new RuntimeException("Erro: token inválido \"" + lexema.toString() + "\"");
                    }
                    break;
                case 1:
                    if(this.isDigito(c)){
                        lexema.append(c);
                        estado = 1;                        
                    }else if(c == '.'){
                        lexema.append(c);
                        estado = 2;
                    }
                    else{
                        this.back();
                        return new Token(lexema.toString(), Token.TIPO_INTEIRO);                        
                    }
                    break;
                case 2:
                    if(this.isDigito(c)){
                        lexema.append(c);
                        estado = 3;
                    }else{
                        throw new RuntimeException("Erro: número float inválido \"" + lexema.toString() + "\"");
                    }
                    break;
                case 3:
                    if(this.isDigito(c)){
                        lexema.append(c);
                        estado = 3;
                    }else{
                        this.back();
                        return new Token(lexema.toString(), Token.TIPO_REAL);
                    }
                    break;
                case 4:
                    if(this.isDigito(c) || this.isLetra(c)){
                        lexema.append(c);
                        estado = 4;
                    }else{
                        if (isReservada(lexema.toString())) {
                            this.back();
                            return new Token(lexema.toString(), Token.TIPO_PALAVRA_RESERVADA);
                        }else
                        this.back();
                        return new Token(lexema.toString(), Token.TIPO_IDENTIFICADOR);
                    }
                    break;
                case 5:
                    this.back();
                    return new Token(lexema.toString(), Token.TIPO_CARACTER_ESPECIAL); 
                case 6:
                    this.back();
                    return new Token(lexema.toString(), Token.TIPO_OPERADOR_ARITMETICO);
                case 7:
                    if(c == '='){
                        lexema.append(c);
                        estado = 8;
                    }else{
                        this.back();
                        return new Token(lexema.toString(), Token.TIPO_OPERADOR_RELACIONAL);
                    }
                    break;
                case 8:
                    this.back();
                    return new Token(lexema.toString(), Token.TIPO_OPERADOR_RELACIONAL);    
                case 9:
                    if(c == '=' || c == '>'){
                        lexema.append(c);
                        estado = 10;
                    }else{
                        this.back();
                        return new Token(lexema.toString(), Token.TIPO_OPERADOR_RELACIONAL);
                    }
                    break;
                case 10:
                    this.back();
                    return new Token(lexema.toString(), Token.TIPO_OPERADOR_RELACIONAL); 
                case 11:
                    if (c == '=') {
                        lexema.append(c);
                        estado = 12;
                    }else{
                        this.back();
                        return new Token(lexema.toString(), Token.TIPO_OPERADOR_ATRIBUICAO);    
                    }
                    break;
                case 12:
                    this.back();
                    return new Token(lexema.toString(), Token.TIPO_OPERADOR_RELACIONAL);    
                case 13:
                    if (this.isDigito(c) || this.isLetra(c)) {
                        lexema.append(c);
                        estado = 14;
                    }else{
                        throw new RuntimeException("Erro: char invalido \"" + lexema.toString() + "\"");
                    }
                    break;
                case 14:
                    if (isAspas(c)) {
                        lexema.append(c);
                        estado = 15;
                    }else{
                        throw new RuntimeException("Erro: char invalido \"" + lexema.toString() + "\"");
                    }
                    break;
                case 15:
                    this.back();
                    return new Token(lexema.toString(), Token.TIPO_CHAR); 
                case 99:
                    return new Token(lexema.toString(), Token.TIPO_FIM_CODIGO);
            }
        }                
        return token;
    }   
}