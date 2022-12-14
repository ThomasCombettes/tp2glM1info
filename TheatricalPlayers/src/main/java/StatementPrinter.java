import java.text.NumberFormat;
import java.util.*;



public class StatementPrinter {

 

  // ecrire en HTML (même si c'est pas beau je l'avoue)
  public static String stringToHTMLString(String string) {
    StringBuffer sb = new StringBuffer(string.length());
    // true if last char was blank
    boolean lastWasBlankChar = false;
    int len = string.length();
    char c;

    for (int i = 0; i < len; i++) {
        c = string.charAt(i);
        if (c == ' ') {
            // blank gets extra work,
            // this solves the problem you get if you replace all
            // blanks with &nbsp;, if you do that you loss 
            // word breaking
            if (lastWasBlankChar) {
                lastWasBlankChar = false;
                sb.append("&nbsp;");
            } else {
                lastWasBlankChar = true;
                sb.append(' ');
            }
        } else {
            lastWasBlankChar = false;
            //
            // HTML Special Chars
            if (c == '"')
                sb.append("&quot;");
            else if (c == '&')
                sb.append("&amp;");
            else if (c == '<')
                sb.append("&lt;");
            else if (c == '>')
                sb.append("&gt;");
            else if (c == '\n')
                // Handle Newline
                sb.append("<br/>");
            else {
                int ci = 0xffff & c;
                if (ci < 160)
                    // nothing special only 7 Bit
                    sb.append(c);
                else {
                    // Not 7 Bit use the unicode system
                    sb.append("&#");
                    sb.append(new Integer(ci).toString());
                    sb.append(';');
                }
            }
        }
    }
    return sb.toString();
}

  public String print(Invoice invoice, Map<String, Play> plays) {
    int totalAmount = 0;
    int volumeCredits = 0;
    String result = String.format("Statement for %s\n", invoice.customer);

    NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);

    for (Performance perf : invoice.performances) {
      Play play = plays.get(perf.playID);
      int thisAmount = play.Amount(perf.audience);

      // add volume credits
      volumeCredits += Math.max(perf.audience - 30, 0);
      // add extra credit for every ten comedy attendees
      if ("comedy".equals(play.type)) volumeCredits += Math.floor(perf.audience / 5);

      // print line for this order
      result += String.format(" - %s: %s (%s seats)\n", play.name, frmt.format(thisAmount / 100), perf.audience);
      totalAmount += thisAmount;


    }  //fin du for 


    result += String.format("Amount owed is %s\n", frmt.format(totalAmount / 100));
    result += String.format("You earned %s credits\n", volumeCredits);
    //writeToFile("Note.txt",  result);
    //result = stringToHTMLString(result);
    //writeToFile("Note.html",  result);
    return result;
  }

}
