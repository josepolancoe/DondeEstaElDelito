package ga.josepolanco.dondeestaeldelito02.Clases;

public class Usuario {
    private String nombre;
    private String apellido;
    private String celular;
    private String edad;
    private String correo;
    private String contraseña01;
    private String contraseña02;

    public Usuario(){}

    public Usuario(String nombre, String apellido, String celular, String edad, String correo, String contraseña01, String contraseña02) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.celular = celular;
        this.edad = edad;
        this.correo = correo;
        this.contraseña01 = contraseña01;
        this.contraseña02 = contraseña02;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña01() {
        return contraseña01;
    }

    public void setContraseña01(String contraseña01) {
        this.contraseña01 = contraseña01;
    }

    public String getContraseña02() {
        return contraseña02;
    }

    public void setContraseña02(String contraseña02) {
        this.contraseña02 = contraseña02;
    }
}
