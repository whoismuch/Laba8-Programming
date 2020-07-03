package client;

import common.generatedClasses.Coordinates;
import common.generatedClasses.Location;
import common.generatedClasses.Route;

import java.time.ZonedDateTime;

public class FullRoute{

    private String name; //Поле не может быть null, Строка не может быть пустой
    /**
     * Поле уникальный номер маршрута
     */
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    /**
     * Поле координаты(текущее местоположение)
     */

    private Long coordinateX;

    private int coordinateY;
    /**
     * Поле дата и время GMT
     */
    private ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    /**
     * Поле начальная локация маршрута
     */
    private String fromName;

    private long fromX;

    private Long fromY;

    private String toName;

    private long toX;

    private Long toY;


    /**
     * Поле конечная локация маршрута
     */
    /**
     * Поле длина маршрута(расстояние)
     */
    private Float distance; //Поле не может быть null, Значение поля должно быть больше 1

    private String username;

    public FullRoute (Route route) {
        id = route.getId();
        username = route.getUsername();
        name = route.getName();
        coordinateX = route.getCoordinates().getX();
        coordinateY = route.getCoordinates().getY();
        creationDate = route.getCreationDate();
        fromName = route.getFrom().getName();
        fromX = route.getFrom().getX();
        fromY = route.getFrom().getY();
        toName = route.getTo().getName();
        toX = route.getTo().getX();
        toY = route.getTo().getY();
        distance = route.getDistance();
    }

    public String getName ( ) {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public Long getId ( ) {
        return id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public Long getCoordinateX ( ) {
        return coordinateX;
    }

    public void setCoordinateX (Long coordinateX) {
        this.coordinateX = coordinateX;
    }

    public int getCoordinateY ( ) {
        return coordinateY;
    }

    public void setCoordinateY (int coordinateY) {
        this.coordinateY = coordinateY;
    }

    public ZonedDateTime getCreationDate ( ) {
        return creationDate;
    }

    public void setCreationDate (ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getFromName ( ) {
        return fromName;
    }

    public void setFromName (String fromName) {
        this.fromName = fromName;
    }

    public long getFromX ( ) {
        return fromX;
    }

    public void setFromX (long fromX) {
        this.fromX = fromX;
    }

    public Long getFromY ( ) {
        return fromY;
    }

    public void setFromY (Long fromY) {
        this.fromY = fromY;
    }

    public String getToName ( ) {
        return toName;
    }

    public void setToName (String toName) {
        this.toName = toName;
    }

    public long getToX ( ) {
        return toX;
    }

    public void setToX (long toX) {
        this.toX = toX;
    }

    public Long getToY ( ) {
        return toY;
    }

    public void setToY (Long toY) {
        this.toY = toY;
    }

    public Float getDistance ( ) {
        return distance;
    }

    public void setDistance (Float distance) {
        this.distance = distance;
    }

    public String getUsername ( ) {
        return username;
    }

    public void setUsername (String username) {
        this.username = username;
    }
}
