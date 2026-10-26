package io.github.jihoon135.api.ratification;

import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/treaties")
public class RatificationController {

    private final RatificationService service;

    public RatificationController(RatificationService service) {
        this.service = service;
    }

    @Operation(summary = "조약별 비준 현황 (지구본 색칠용)")
    @ApiResponse(responseCode = "200", description = "비준 현황 (행이 0건이어도 200)")
    @ApiResponse(responseCode = "404", description = "알 수 없는 조약 코드",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @GetMapping(path = "/{code}/ratifications", produces = MediaType.APPLICATION_JSON_VALUE)
    public TreatyRatificationsResponse ratifications(@PathVariable String code) {
        return service.getRatifications(code);
    }
}
