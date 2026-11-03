package ch.bzz.controller;

import ch.bzz.generated.api.AccountApi;
import ch.bzz.generated.model.Account;
import ch.bzz.generated.model.UpdateAccountsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class AccountApiController implements AccountApi {

    @Override
    public ResponseEntity<List<Account>> getAccounts() {
        log.debug("getAccounts not yet implemented");
        return ResponseEntity.status(501).build();
    }

    @Override
    public ResponseEntity<Void> updateAccounts(UpdateAccountsRequest updateAccountsRequest) {
        log.debug("updateAccounts not yet implemented");
        return ResponseEntity.status(501).build();
    }
}
