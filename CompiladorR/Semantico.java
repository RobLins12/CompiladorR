package CompiladorR;

import java.util.HashMap;

public class Semantico {
    
    private Lexico scanner;
    private Token currToken;
    private HashMap<String, String> vMap = new HashMap<>();
    private String idAtual;

    public Semantico(Lexico scanner){
        this.scanner = scanner;
    }

    public void programa(){
        currToken = scanner.nextToken();
        if (currToken.getLexema().equals("int")) {
            currToken = scanner.nextToken();
            if (currToken.getLexema().equals("main")) {
                currToken = scanner.nextToken();
                if (currToken.getLexema().equals("(")) {
                    currToken = scanner.nextToken();
                    if (currToken.getLexema().equals(")")) {
                        currToken = scanner.nextToken();
                        bloco();
                    } else {
                        throw new RuntimeException("Erro na iniciação do programa Esperado: ) int Got: " + currToken.getLexema() + " at line " + Lexico.linha);
                    }
                } else {
                    throw new RuntimeException("Erro na iniciação do programa Esperado: ( Got: " + currToken.getLexema() + " at line " + Lexico.linha);
                }
            } else {
                throw new RuntimeException("Erro na iniciação do programa Esperado: main Got: " + currToken.getLexema() + " at line " + Lexico.linha);
            }
        }else{
            throw new RuntimeException("Erro na iniciação do programa Esperado: int Got: " + currToken.getLexema() + " at line " + Lexico.linha);
        }
    }

    public void bloco() {
        if (currToken.getLexema().equals("{")) {
            currToken = scanner.nextToken();
            while (currToken.getTipo() != Token.TIPO_FIM_CODIGO && !currToken.getLexema().equals("}")) {
                if (currToken.getLexema().equals("int")|| 
                currToken.getLexema().equals("float") || 
                currToken.getLexema().equals("char")) 
                {
                   decl_var(); 
                }
                else{
                   comando(); 
                }
            }
            if (currToken.getLexema().equals("}")) {
                currToken = scanner.nextToken();
            }else{
                throw new RuntimeException("Bloco mal formatado Esperado: } Got: " + currToken.getLexema() + " at line " + Lexico.linha);
            } 
        }else{
            throw new RuntimeException("Bloco mal formatado Esperado: { Got: " + currToken.getLexema() + " at line " + Lexico.linha);
        }
    }

    public void comando() {
        if(currToken.getLexema().equals("if")){
            condicional();
        }
        else if(currToken.getLexema().equals("while")){
            iteração();
        }
        else if(currToken.getTipo() == 3 || currToken.getLexema().equals("{")) {
            comandoBasico();
        }
    }

    public void comandoBasico() {
        if (currToken.getTipo() == 3) {
            atribuição();
        } else if(currToken.getLexema().equals("{")) {
            bloco();
        }else{
            throw new RuntimeException("Comando básico inválido Esperado: 'Atribuicao' ou  'Bloco' Got: " + currToken.getLexema() + " at line " + Lexico.linha);
        }
    }

    public void atribuição() {
        if (currToken.getTipo() == 3) {
            String Id = currToken.getLexema();
            this.idAtual = Id;
            String tipo = vMap.get(Id);
            if (tipo == null) {
                throw new RuntimeException("Erro na analise semântica, variável não existe at line "  + Lexico.linha);
            }
            currToken = scanner.nextToken();
            if (currToken.getLexema().equals("=")) {
                currToken = scanner.nextToken();
                expr_arit();
                if (currToken.getLexema().equals(";")) {
                    currToken = scanner.nextToken();
                } else{
                    throw new RuntimeException("Atribuicao inválida Esperado: ; Got: " + currToken.getLexema() + " at line " + Lexico.linha);
                }
            } else {
                throw new RuntimeException("Atribuicao inválida Esperado: = Got: " + currToken.getLexema() + " at line " + Lexico.linha);
            }
        }else{
            throw new RuntimeException("Atribuicao inválida Esperado: 'Identificador' Got: " + currToken.getLexema() + " at line " + Lexico.linha);
        }
    }

    public void iteração() {
        currToken = scanner.nextToken();
        if (currToken.getLexema().equals("(")) {
            currToken = scanner.nextToken();
            expr_relacional();
            if (currToken.getLexema().equals(")")) {
                currToken = scanner.nextToken();
            }else{
                throw new RuntimeException("Iteração mal formatada Esperado: ) Got: " + currToken.getLexema() + " at line " + Lexico.linha);
            }
        }else{
            throw new RuntimeException("Iteração mal formatada Esperado: ( Got: " + currToken.getLexema() + " at line " + Lexico.linha);
        }
    }

    public void condicional() {
        currToken = scanner.nextToken();
        if (currToken.getLexema().equals("(")) {
            currToken = scanner.nextToken();
                expr_relacional();
                if (currToken.getLexema().equals(")")) {
                    currToken = scanner.nextToken();
                    comando();
                    if (currToken.getLexema().equals("else")) {
                        currToken = scanner.nextToken();
                        comando();
                    }
                } else {
                    throw new RuntimeException("Condicional mal formatada Esperado: ) Got: " + currToken.getLexema() + " at line " + Lexico.linha);
                }
            }else{
                throw new RuntimeException("Condicional mal formatada Esperado: ( Got: " + currToken.getLexema() + " at line " + Lexico.linha);
            }
    }

    public void expr_relacional() {
        expr_arit();
        if (currToken.getTipo() == 4) {
            currToken = scanner.nextToken();
            expr_arit();
        }else{
            throw new RuntimeException("Expressão relacional mal formatada Esperado: 'Operador relacional' Got: " + currToken.getLexema() + " at line " + Lexico.linha);
        }
    }

    public void expr_arit() {
        termo();
        EA();
    }

    public void EA() {
        if (currToken.getLexema().equals("+") || currToken.getLexema().equals("-")) {
            currToken = scanner.nextToken();
            termo();
            EA();
        }
    }

    public void termo() {
        fator();
        TL();
    }

    public void TL() {
        if (currToken.getLexema().equals("*") || currToken.getLexema().equals("/")) {
            currToken = scanner.nextToken();
            fator();
            TL();
        }
    }

    public void fator() {
        if (currToken.getLexema().equals("(")) {
            currToken = scanner.nextToken();
            expr_relacional();
            if (currToken.getLexema().equals(")")) {
                currToken = scanner.nextToken();
            }else{
                throw new RuntimeException("Fator mal formatado Esperado: ) Got: " + currToken.getLexema() + " at line " + Lexico.linha);
            }
        }else if (currToken.getTipo() == 0 || currToken.getTipo() == 1 || currToken.getTipo() == 2 || currToken.getTipo() == 3) {
            String tipo = vMap.get(this.idAtual);
            if (tipo == null) {
                currToken = scanner.nextToken();
            } else if (currToken.getTipo() == 0 && tipo.equals("int")) {
                currToken = scanner.nextToken();
            } else if(currToken.getTipo() == 1 && tipo.equals("float")){
                currToken = scanner.nextToken();
            } else if(currToken.getTipo() == 2 && tipo.equals("char")){
                currToken = scanner.nextToken();
            } else if(currToken.getTipo() == 3 && tipo.equals(vMap.get(currToken.getLexema()))){
                currToken = scanner.nextToken();
            }else{
                throw new RuntimeException("Erro na analise Semântica, tipos de variável não condizem at line " + Lexico.linha);
            }
        }else{
            throw new RuntimeException("Fator mal formatado Esperado: ( ou 'Id' ou 'Variavel' Got: " + currToken.getLexema() + " at line " + Lexico.linha);
        }
    }

    public void decl_var() {
        if ((currToken.getLexema().equals("int")||
        currToken.getLexema().equals("float") ||
        currToken.getLexema().equals("char") )) {
            String Tipo = currToken.getLexema();
            currToken = scanner.nextToken();   
            if (currToken.getTipo() == Token.TIPO_IDENTIFICADOR) {
                String Id = currToken.getLexema();
                currToken = scanner.nextToken();  
                if (currToken.getLexema().equals(";")) {
                    if (vMap.get(Id) == null) {
                        vMap.put(Id, Tipo);
                    }else{
                        throw new RuntimeException("Váriaveis repetidas at line " + Lexico.linha);
                    } 
                    currToken = scanner.nextToken();  
                }else{
                    throw new RuntimeException("Declaracao de variável inválida Esperado: ; Got: " + currToken.getLexema() + " at line " + Lexico.linha);
                }
            } 
            else{
                throw new RuntimeException("Declaracao de variável inválida Esperado: 'Identificador' Got: " + currToken.getLexema() + " at line " + Lexico.linha);
            }
        }else{
            throw new RuntimeException("Declaracao de variável inválida Esperado: 'Tipo de Variável' Got: " + currToken.getLexema() + " at line " + Lexico.linha);
        }
    }
}
