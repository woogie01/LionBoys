package likelion.lionboys.domain.party.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import likelion.lionboys.domain.participant.dto.SignUpReq;
import likelion.lionboys.domain.participant.dto.SignUpResp;
import likelion.lionboys.domain.round.dto.SetSecretaryReq;
import likelion.lionboys.domain.round.dto.SetSecretaryResp;
import likelion.lionboys.domain.participant.service.ParticipantService;
import likelion.lionboys.domain.party.dto.CreatePartyReq;
import likelion.lionboys.domain.party.dto.CreatePartyResp;
import likelion.lionboys.domain.party.service.PartyService;
import likelion.lionboys.domain.round.dto.CreateRoundReq;
import likelion.lionboys.domain.round.dto.CreateRoundResp;
import likelion.lionboys.domain.round.service.RoundService;
import likelion.lionboys.global.response.ApiResponse;
import likelion.lionboys.global.support.CurrentParticipantId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/party")
@Tag(name = "Party API")
public class PartyController {
    private final PartyService partyService;
    private final ParticipantService participantService;
    private final RoundService roundService;

    @Operation(summary = "Party 생성")
    @PostMapping
    public ResponseEntity<ApiResponse<CreatePartyResp>> create(@Valid @RequestBody CreatePartyReq req) {
        var resp = partyService.createPartyWithFirstRound(req);
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{partyId}")
                .buildAndExpand(resp.partyId())
                .toUri();

        return ResponseEntity.created(location)
                .body(ApiResponse.success(resp));
    }

    @Operation(summary = "Party 참가")
    @PostMapping("/{partyId}/join")
    public ResponseEntity<ApiResponse<SignUpResp>> joinParty(
            @PathVariable Long partyId,
            @Valid @RequestBody SignUpReq req
    ) {
        var resp = participantService.signUp(partyId, req);
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{participantId}")
                .buildAndExpand(resp.participantId())
                .toUri();

        return ResponseEntity.created(location)
                .body(ApiResponse.success(resp));
    }

    @Operation(summary = "Party 차수 추가")
    @PostMapping("/{partyId}/round")
    public ResponseEntity<ApiResponse<CreateRoundResp>> createRound(
            @PathVariable Long partyId,
            @Valid @RequestBody CreateRoundReq req,
            @CurrentParticipantId Long secretaryId
    ) {
        var resp = roundService.createRound(partyId, req, secretaryId);
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{roundId}")
                .buildAndExpand(resp.roundId())
                .toUri();

        return ResponseEntity.created(location)
                .body(ApiResponse.success(resp));
    }

    @Operation(summary = "Round 총무 지정")
    @PutMapping("/{partyId}/round/{roundId}")
    public ResponseEntity<ApiResponse<SetSecretaryResp>> updateSecretary(
            @PathVariable Long partyId,
            @PathVariable Long roundId,
            @Valid @RequestBody SetSecretaryReq req
    ) {
        var resp = roundService.setSecretary(partyId, roundId, req);
        return ResponseEntity.ok(ApiResponse.success(resp));
    }

}