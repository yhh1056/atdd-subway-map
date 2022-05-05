package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.dao.LineDao;
import wooteco.subway.domain.Line;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.StationResponse;

@DisplayName("노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("노선을 등록한다.")
    void save() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.body().jsonPath().getString("name")).isEqualTo("신분당선");
        assertThat(response.body().jsonPath().getString("color")).isEqualTo("bg-red-600");
    }

    @Test
    @DisplayName("노선 생성시 빈 값일 경우 400응답을 한다.")
    void saveEmpty() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "");
        params.put("color", "");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines")
            .then().log().all()
            .extract();

        assertThat(response.statusCode()).isEqualTo(400);
    }

    @Test
    @DisplayName("노선을 id로 조회한다.")
    void showLine() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "blue");
        ExtractableResponse<Response> saveResponse = RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines")
            .then().log().all()
            .extract();
        long id = saveResponse.body().jsonPath().getLong("id");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .when()
            .get("/lines/{id}", id)
            .then()
            .log().all().extract();

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.body().jsonPath().getString("name")).isEqualTo("1호선");
        assertThat(response.body().jsonPath().getString("color")).isEqualTo("blue");
    }

    @Test
    @DisplayName("없는 노선을 조회시 에러")
    void notFindLine() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .when()
            .get("/lines/{id}", 1)
            .then()
            .log().all().extract();

        assertThat(response.statusCode()).isEqualTo(404);
    }

    @Test
    @DisplayName("노선 목록을 조회한다.")
    void showLines() {
        /// given
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", "1호선");
        params1.put("color", "blue");
        RestAssured.given().log().all()
            .body(params1)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines")
            .then().log().all()
            .extract();

        Map<String, String> params2 = new HashMap<>();
        params2.put("name", "2호선");
        params2.put("color", "green");
        RestAssured.given().log().all()
            .body(params2)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines")
            .then().log().all()
            .extract();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .when()
            .get("/lines")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        List<LineResponse> responses = response.body().jsonPath().getList(".", LineResponse.class);
        assertThat(responses).extracting("name").isEqualTo(List.of("1호선", "2호선"));
    }

    @Test
    @DisplayName("노선을 id로 수정한다.")
    void modify() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "blue");
        ExtractableResponse<Response> saveResponse = RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines")
            .then().log().all()
            .extract();
        long id = saveResponse.body().jsonPath().getLong("id");

        Map<String, String> params2 = new HashMap<>();
        params2.put("name", "2호선");
        params2.put("color", "green");
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params2)
            .when()
            .put("/lines/{id}", id)
            .then()
            .log().all().extract();

        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("노선 수정시 빈 값일 경우 400응답을 한다.")
    void modifyEmpty() {
        Map<String, String> prams = new HashMap<>();
        prams.put("name", "");
        prams.put("color", "");

        Line line = LineDao.save(new Line("1호선", "blue"));

        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(prams)
            .when()
            .put("/lines/{id}", line.getId())
            .then()
            .log().all().extract();

        assertThat(response.statusCode()).isEqualTo(400);
    }

    @Test
    @DisplayName("수정시 노선이 중복될 경우 400응답을 한다.")
    void duplicateUpdate() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "blue");
        RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines")
            .then().log().all()
            .extract();

        Map<String, String> params2 = new HashMap<>();
        params2.put("name", "2호선");
        params2.put("color", "green");
        ExtractableResponse<Response> saveResponse = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params2)
            .when()
            .post("/lines")
            .then()
            .log().all().extract();
        long id = saveResponse.body().jsonPath().getLong("id");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when()
            .put("/lines/{id}", id)
            .then()
            .log().all().extract();

        assertThat(response.statusCode()).isEqualTo(400);
    }

    @Test
    @DisplayName("노선을 id로 삭제한다.")
    void deleteById() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "blue");
        ExtractableResponse<Response> saveResponse = RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines")
            .then().log().all()
            .extract();
        long id = saveResponse.body().jsonPath().getLong("id");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .when()
            .delete("/lines/{id}", id)
            .then()
            .log().all().extract();

        assertThat(response.statusCode()).isEqualTo(204);
    }
}
