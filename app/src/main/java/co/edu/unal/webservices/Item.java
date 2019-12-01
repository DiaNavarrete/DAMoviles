package co.edu.unal.webservices;

public class Item {
    public String getOperador() {
        return operador;
    }

    String operador;

    public String getValor() {
        return String.format("%.5f",valor);
    }

    public String getKpi() {
        return kpi;
    }

    Double valor;
    String kpi;

    public Item(String operador, Double valor, String kpi, String unidad) {

        this.operador = operador;
        this.valor = valor;
        this.kpi = kpi + " ("+unidad+")";
    }


    public Item() {
    }

    @Override
    public String toString(){
        return operador +": " + kpi + "= " + valor ;
    }
}
