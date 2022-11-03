public class Play {

  public String name;
  public String type;
  public int thisAmount = 0;

  public Play(String name, String type) {
    this.name = name;
    this.type = type;
  }

  public int Amount(int perf){
    
    switch (this.type) {
        case "tragedy":
          thisAmount = 40000;
          if (perf > 30) {
            thisAmount += 1000 * (perf - 30);
          }
          break;
        case "comedy":
          thisAmount = 30000;
          if (perf > 20) {
            thisAmount += 10000 + 500 * (perf - 20);
          }
          thisAmount += 300 * perf;
          break;
        default:
          throw new Error("unknown type: ${this.type}");
      }
    return thisAmount;
  }
}
