package likelion.lionboys.domain.party.controller;

import likelion.lionboys.domain.party.dto.CreatePartyReq;
import likelion.lionboys.domain.party.dto.CreatePartyResp;
import likelion.lionboys.domain.party.service.PartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/parties")
public class PartyController {
    private final PartyService partyService;

    @PostMapping
    public ResponseEntity<CreatePartyResp> create(@RequestBody CreatePartyReq req) {
        CreatePartyResp resp = partyService.createPartyWithFirstRound(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}