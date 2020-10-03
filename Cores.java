import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Cores {
    //Εδώ υλοποιήται η Λειτουργία Α του προβλήματος
    public static int functionA(ArrayList<Integer> cores, int client){
        int arr[] = new int[client + 1]; // Σε αυτό τον πίνκα θα αποθηκεύσω το ελάχιστο πλήθος από VMs για έναν πελάτη για κάθε πλήθος πυρήνων
        arr[0] = 0;
        for (int i = 1; i <= client; i++) // Αρχικοποιώ το πίνακα εκτός από το πρώτο στοιχείο με την μέγιστη τιμή που μπορεί να πάρει ένας ακέραιος
            arr[i] = Integer.MAX_VALUE;
        for (int i = 1; i <= client; i++)       // Σε αυτό το σημείο υπολογίζω το ελάχιστο πλήθος για κάθε τιμή από το 1 έως το το ποσό των πυρήνων
        {                                       // που ζητά ο πελάτης
            for (int j = 0; j < cores.size(); j++)
                if (cores.get(j) <= i)
                {
                    int x = arr[i - cores.get(j)];
                    if (x != Float.MAX_VALUE
                            && x + 1 < arr[i])
                        arr[i] = x + 1;
                }
        }
        return arr[client];       //Επιστρέφω το πλήθος των ελάχιστων VMs
    }
    // Εδώ υλοποιήται η Λειτουργία Β του προβλήματος
    public static float functionB(int totalCor, ArrayList<Float> prices, ArrayList<Integer> numberOfCores){
        float[][] arr = new float[numberOfCores.size()+1][totalCor+1];  // Δημιουργώ ένα δισδιάστατο πίνακα πραγματικών
        for (int i = 0; i<= numberOfCores.size(); i++) {        // Διατρέχω τον πίνακα γεμίζοντας τα κελιά με το μέγιστο ποσο
            for (int w = 0; w<= totalCor; w++) {        // που μπορεί να κερδίσει η εταιρεία άν είχε να δώσει λιγότερους πυρήνες
                if (i == 0 || w == 0)               // και λιγότερους πελάτες. Έτσι συνδυάζονρας τις λύσεις άλλων μικρότερων περιπτώσεων
                    arr[i][w] = 0;              // καταλήγω στο επιθυμητό αποτέλεσμα
                else if (numberOfCores.get(i-1) <= w)
                    arr[i][w] = Math.max(prices.get(i-1)*numberOfCores.get(i-1) + arr[i - 1][w - numberOfCores.get(i-1)], arr[i - 1][w]); // Πολλαπλασιάζω και με την τιμή
                else
                    arr[i][w] = arr[i - 1][w];
            }
        }
        return arr[numberOfCores.size()][totalCor]; // Και επιστρέφω την τιμή
    }


    public static void main(String[] args) throws IOException {
        try {
            FileInputStream fstream = new FileInputStream(args[0]);             //Διαβάζω το αρχείο arg[0] που περνά σαν όρισμα στην συναρτηση main
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            ArrayList<Integer> listOfPairs = new ArrayList<Integer>();      // Σε αυτή τη δομή θα βάλω τους πυρήνες που ζητάει ο κάθε πελάτης
            ArrayList<Float> listPrice = new ArrayList<Float>();        // Το ποσό που δίνει για κάθε πυρήνα
            int totalCores=0;
            String strLine;
            int i = 0;
            while ((strLine = br.readLine()) != null) {         //Όσο το αρχείο δεν έχει τελειώσει διαβάζω τις γραμμές του
                if(i==0){
                    totalCores = Integer.parseInt(strLine);
                } else {
                    String[] tokens = strLine.split(" ");
                    int a = Integer.parseInt(tokens[0]);
                    float b = Float.valueOf(tokens[1]).floatValue();
                    listOfPairs.add(a);             // Αποθηκεύω σε δύο διαφορετικές λίστες τους δύο αριθμούς της κάθε γραμμής
                    listPrice.add(b);
                }
                i++;
            }
            ArrayList<Integer> cores = new ArrayList<Integer>();        // Αποθηκεύω σε ένα ArrayList τις 4 τιμές των πυρήνων που παρέχει η εταιρεία
            cores.add(1);
            cores.add(2);
            cores.add(7);
            cores.add(11);

            ArrayList<Integer> minVMs = new ArrayList<Integer>();
            for(int j=0;j<listOfPairs.size();j++){
                minVMs.add(functionA(cores, listOfPairs.get(j)));
            }
            for(int j=0;j<minVMs.size();j++){
                System.out.println("Client "+(j+1)+": "+minVMs.get(j)+" VMs");  // Εμφανίζω το ελάχιστο πλήθος από VMs ανά πελάτη
            }
            System.out.print("Total amount: "+functionB(totalCores, listPrice, listOfPairs));        //Εμφανίζω το μέγιστο ποσό πληρωμής
        }
        catch(IOException ioException)                  //Σε αυτά τα τρία catch Exception εμφανίζει στην κονσόλα πιθανά λάθη που μπορεί να γίνουν
        {                                                  // στο άνοιγμα και στο διάβασμα του αρχείου
            System.err.println("Error opening file. Terminating.");
            System.exit(1);
        }
        catch(NoSuchElementException elementException)
        {
            System.out.println("File improperly formed. Terminating");
        }
        catch(IllegalStateException stateException)
        {
            System.err.println("Error reading from file. Terminating.");
        }
    }
}
