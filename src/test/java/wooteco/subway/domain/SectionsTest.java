package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.domain.fixtures.TestFixtures.강남;
import static wooteco.subway.domain.fixtures.TestFixtures.건대;
import static wooteco.subway.domain.fixtures.TestFixtures.삼성;
import static wooteco.subway.domain.fixtures.TestFixtures.성수;
import static wooteco.subway.domain.fixtures.TestFixtures.왕십리;
import static wooteco.subway.domain.fixtures.TestFixtures.잠실;
import static wooteco.subway.domain.fixtures.TestFixtures.합정;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.fixtures.TestFixtures;

class SectionsTest {

    @Test
    @DisplayName("구간들을 생성시 정렬된다.")
    void create() {
        Sections sections = new Sections(getSections(new Line("2호선", "green")));

        List<Section> values = sections.getValues();

        assertThat(values.get(0).getUpStation().getName()).isEqualTo("강남");
        assertThat(values.get(1).getUpStation().getName()).isEqualTo("잠실");
        assertThat(values.get(2).getUpStation().getName()).isEqualTo("성수");
    }

    @Test
    @DisplayName("상행역을 기준으로 구간을 추가하면 분리된 구간들을 반환한다.")
    void addSectionByUpStation() {
        Line line = new Line("2호선", "green");
        Sections 기존_구간 = new Sections(getSections(line));

        Section 추가할_구간 = new Section(line, 강남, 삼성, 5);
        List<Section> 추가된_구간 = 기존_구간.findUpdateSections(추가할_구간);

        List<Section> sections = new Sections(추가된_구간).getValues();
        assertThat(sections.get(0).getUpStation().getName()).isEqualTo("강남");
        assertThat(sections.get(0).getDistance()).isEqualTo(5);
        assertThat(sections.get(1).getUpStation().getName()).isEqualTo("삼성");
        assertThat(sections.get(1).getDistance()).isEqualTo(7);
    }

    @Test
    @DisplayName("하행역 기준으로 구간을 추가하면 분리된 구간들을 반환한다.")
    void addSectionByDownStation() {
        Line line = new Line("2호선", "green");
        Sections 기존_구간 = new Sections(getSections(line));

        Section 추가할_구간 = new Section(line, 삼성, 잠실, 5);
        List<Section> 추가된_구간 = 기존_구간.findUpdateSections(추가할_구간);

        List<Section> sections = new Sections(추가된_구간).getValues();
        assertThat(sections.get(0).getUpStation().getName()).isEqualTo("강남");
        assertThat(sections.get(0).getDistance()).isEqualTo(7);
        assertThat(sections.get(1).getUpStation().getName()).isEqualTo("삼성");
        assertThat(sections.get(1).getDistance()).isEqualTo(5);
    }

    @Test
    @DisplayName("상행 종점을 추가한다.")
    void addUpStation() {
        Line line = new Line("2호선", "green");
        Sections 기존_구간 = new Sections(getSections(line));

        Section 추가할_구간 = new Section(line, 합정, 강남, 5);
        List<Section> 추가된_구간 = 기존_구간.findUpdateSections(추가할_구간);

        List<Section> sections = new Sections(추가된_구간).getValues();
        assertThat(sections.get(0).getUpStation().getName()).isEqualTo("합정");
        assertThat(sections.get(0).getDistance()).isEqualTo(5);
    }

    private List<Section> getSections(Line line) {
        return List.of(
            new Section(line, 강남, 잠실, 12),
            new Section(line, 잠실, 성수, 12),
            new Section(line, 성수, 왕십리, 12)
        );
    }
}
