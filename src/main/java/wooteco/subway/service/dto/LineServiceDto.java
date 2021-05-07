package wooteco.subway.service.dto;

public class LineServiceDto {

    private final Long id;
    private final String name;
    private final String color;

    public LineServiceDto(final Long id) {
        this(id, null, null);
    }

    public LineServiceDto(final String name, final String color) {
        this(null, name, color);
    }

    public LineServiceDto(final Long id, final String name, final String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
