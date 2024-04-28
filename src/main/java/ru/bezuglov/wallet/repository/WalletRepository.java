package ru.bezuglov.wallet.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import ru.bezuglov.wallet.model.Wallet;

import java.util.UUID;

public interface WalletRepository extends JpaRepositoryImplementation<Wallet, UUID> {
}
