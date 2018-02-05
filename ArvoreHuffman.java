/**
 * Trabalho de Algoritmos e Estruturas de Dados III
 * Professor Marcos Kutova
 * PUC Minas
 * <p>
 * Daniel Gunna, Felipe Coelho, Leonardo Palis
 */


/**
 * Implementacao da arvore de huffman
 */

public class ArvoreHuffman implements Serializable {
    
    //ID para serializar
    public static final long serialVersionUID = 0x1;
    
    //Para cada caractere, o seu correspondente na arvore
    private String[] codificacao;
    
    //Map para Strings codificadas, para os caracteres que elas representam. (Nao sera serializado)
    private transient Map<String, Character> auxiliarDecodificar;
    
    //Array para contar a quantidade de ocorrencias de cada caractere na tabela ASCII
    static int[] counts = new int[256];
    
    //Char lido da tabel
    static char ch;
    
    /**
     * Metodo Construtor da Arvore de Huffman
     *
     * @param scan Scanner com caracteres ASCII
     */
    
    public ArvoreHuffman(Scanner scan) {
        
        // conta os caracteres linha por linha
        while (scan.hasNextLine()) {
            
            //Pega a linha
            String line = scan.nextLine();
            //Repete ate o final da linha
            for (int i = 0; i < line.length(); i++) {
                
                ch = line.charAt(i);
                if (ch >= 0 && ch <= 255) {
                    counts[ch]++; //incrementa a frequencia no vetor
                }
                
            }
            
            //incrementa o caractere "nova linha" para cada linha lida
            counts['\n']++;
        }
        
        
        //Heap de prioridades, colocando todos os nos com um caractere antes
        PriorityQueue<No> q = new PriorityQueue<No>(256, new No.FrequencyComparator());
        for (char i = 0; i < 256; i++) {
            No n = new Folha(i, counts[i]);
            q.add(n);
        }
        
        // Combinando os nodos com menor requencia ate qe apenas a raiz sobre
        while (q.size() > 1) {
            No right = q.remove();
            No left = q.remove();
            
            No p = new No(left, right);
            q.add(p);
        }
        
        // Mapa de caracteres para seus respecitivos codigos, a partir da arvore
        codificacao = new String[256];
        auxiliarDecodificar = new HashMap<String, Character>();
        
        q.remove().processaCodigo("", codificacao);
        inversaoDados();
    }
    
    /**
     * Metodo que retorna o codigo do char baseado na arvore de huffman
     */
    public String encode(char ch) {
        if (ch >= 0 && ch <= 255) {
            return codificacao[ch];
        } else {
            return "";
        }
    }
    
    /*
     * Faz a inversao dos codigo do Map
     */
    private void inversaoDados() {
        for (char ch = 0; ch < 256; ch++) {
            auxiliarDecodificar.put(codificacao[ch], ch);
        }
    }
    
    /**
     * Retorna o decoder para a arvore criada
     */
    public Decodifica_String decoder() {
        return new Decodifica_String();
    }
    
    
    /**
     * As celulas da arvore de Huffman.
     */
    private static class No {
        private int freq; //frequencia de aparicao no texto
        private No left; //filho esq
        private No right; //filho direito
        
        protected No(int f) {
            freq = f;
        }
        
        public No(No l, No r) {
            freq = l.freq + r.freq;
            left = l;
            right = r;
        }
        
        public void processaCodigo(String r, String[] e) {
            left.processaCodigo(r + "0", e);
            right.processaCodigo(r + "1", e);
        }
        
        
        private static class FrequencyComparator implements Comparator<No> {
            @Override
            public int compare(No n1, No n2) {
                return n1.freq - n2.freq;
            }
        }
    }
    
    
    //Folha na arvore de huffman
    private class Folha extends No {
        private char ch;
        
        private Folha(char c, int f) {
            super(f);
            ch = c;
        }
        
        @Override
        public void processaCodigo(String r, String[] e) {
            e[ch] = r;
        }
    }
    
    
    public class Decodifica_String {
        /*
         * Percorre a arvore bit a bit para efetuar o processo de decodificacao
         */
        private StringBuilder caminhoDeDados;
        
        /**
         * Construtor decoder
         */
        public Decodifica_String() {
            caminhoDeDados = new StringBuilder();
        }
        
    }
    
    public static void main(String[] args) throws Exception {
        
        ArvoreHuffman t = new ArvoreHuffman(new Scanner(new File("doc10.txt"))); //Documento de entrada
        PrintWriter writer = new PrintWriter("doc10r.txt", "Unicode");            //Documento de saida
        double soma = 0;
        
        
        //For para percorrer todos os caracteres  
        for (char i = 0; i < 256; i++) {
            if (counts[i] != 0) {
                writer.println(t.encode(i));
            }
        }
        
        writer.close();
        
    }
}



