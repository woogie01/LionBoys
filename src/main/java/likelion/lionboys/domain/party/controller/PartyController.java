package likelion.lionboys.domain.party.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import likelion.lionboys.domain.party.dto.CreatePartyReq;
import likelion.lionboys.domain.party.dto.CreatePartyResp;
import likelion.lionboys.domain.party.service.PartyService;
import likelion.lionboys.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/party")
@Tag(name = "Party API", description = "Party 생성 및 조회")
public class PartyController {
    private final PartyService partyService;

    @Operation(summary = "Party 생성")
    @PostMapping
    public ResponseEntity<ApiResponse<CreatePartyResp>> create(@Valid @RequestBody CreatePartyReq req) {
        var resp = partyService.createPartyWithFirstRound(req);
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(resp.partyId())
                .toUri();

        return ResponseEntity.created(location)
                .body(ApiResponse.success(resp));
    }

}