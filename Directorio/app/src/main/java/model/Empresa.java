package model;

public class Empresa {
    private long Id;
    private String nombre;
    private String url;
    private int tel;
    private String email;
    private String prod_serv;
    private String tipo;

    public Empresa(long id, String nombre, String url, int tel, String email, String prod_serv, String tipo) {
        Id = id;
        this.nombre = nombre;
        this.url = url;
        this.tel = tel;
        this.email = email;
        this.prod_serv = prod_serv;
        this.tipo = tipo;
    }
    public Empresa(){}


    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTel() {
        return tel;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProd_serv() {
        return prod_serv;
    }

    public void setProd_serv(String prod_serv) {
        this.prod_serv = prod_serv;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }



}
