package wooteco.subway.dto;

import java.util.List;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public LineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public LineResponse(Long id, String name, String color) {
        this(id, name, color, null);
    }

    public static LineResponse from(Line savedLine) {
        return new LineResponse(savedLine.getId(), savedLine.getName(), savedLine.getColor());
    }

    public static LineResponse from(Line savedLine, Section section) {
        return new LineResponse(
            savedLine.getId(),
            savedLine.getName(),
            savedLine.getColor(),
            List.of(
                StationResponse.from(section.getUpStation()),
                StationResponse.from(section.getUpStation())
            )
        );
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

    public List<StationResponse> getStations() {
        return stations;
    }
}
