package entidades;

public class Personal {
    private Integer id_personal;
    private Integer id_administracion_zonal;
    private String cedula;
    private String apellidosnombres;
    private String foto;
    private String fecha_nacimiento;
    private String direccion;
    private String telefono_convecional;
    private String celular;
    private String correo_electronico;

    public Integer getId_personal() {
        return id_personal;
    }

    public void setId_personal(Integer id_personal) {
        this.id_personal = id_personal;
    }

    public Integer getId_administracion_zonal() {
        return id_administracion_zonal;
    }

    public void setId_administracion_zonal(Integer id_administracion_zonal) {
        this.id_administracion_zonal = id_administracion_zonal;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getApellidosnombres() {
        return apellidosnombres;
    }

    public void setApellidosnombres(String apellidosnombres) {
        this.apellidosnombres = apellidosnombres;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono_convecional() {
        return telefono_convecional;
    }

    public void setTelefono_convecional(String telefono_convecional) {
        this.telefono_convecional = telefono_convecional;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCorreo_electronico() {
        return correo_electronico;
    }

    public void setCorreo_electronico(String correo_electronico) {
        this.correo_electronico = correo_electronico;
    }
}
