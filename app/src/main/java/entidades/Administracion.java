package entidades;

public class Administracion {
    private Integer id_administracion_zonal;
    private String nombre;
    private String logo;

    public Integer getId_administracion_zonal() {
        return id_administracion_zonal;
    }

    public void setId_administracion_zonal(Integer id_administracion_zonal) {
        this.id_administracion_zonal = id_administracion_zonal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
