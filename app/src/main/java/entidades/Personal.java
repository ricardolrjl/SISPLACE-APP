package entidades;

public class Personal {
    private Integer id_personal;
    private Integer id_administracion_zonal;
    private String cedula;
    private String apellidosnombres;
    private String foto;

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
}
