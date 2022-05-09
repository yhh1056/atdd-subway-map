package wooteco.subway.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(SectionDao.class)
class SectionDaoTest {

    @Autowired
    private SectionDao sectionDao;

    @Test
    @DisplayName("구간을 저장한다.")
    void save() {
        Line line = new Line(1L, "신분당선", "blue");
        Station station1 = new Station(1L, "강남");
        Station station2 = new Station(2L, "광교");
        Section section = new Section(line, station1, station2, 10);
        Section savedSection = sectionDao.save(section);
        assertThat(savedSection.getId()).isNotNull();
    }
}
