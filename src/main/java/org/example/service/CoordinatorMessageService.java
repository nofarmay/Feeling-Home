package org.example.service;

import org.example.model.CoordinatorMessage;
import org.example.model.CommunityActivityScore;
import org.example.repository.CoordinatorMessageRepository;
import org.example.repository.CommunityActivityScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class CoordinatorMessageService {
    private final CoordinatorMessageRepository messageRepository;
    private final CommunityActivityScoreRepository scoreRepository;

    @Autowired
    public CoordinatorMessageService(CoordinatorMessageRepository messageRepository,
                                     CommunityActivityScoreRepository scoreRepository) {
        this.messageRepository = messageRepository;
        this.scoreRepository = scoreRepository;
    }

    public CoordinatorMessage sendMessage(String content, String registrationCode) {
        CoordinatorMessage message = new CoordinatorMessage();
        message.setContent(content);
        message.setRegistrationCode(registrationCode);
        message.setSentAt(new Date());
        message.setStatus("SENT");

        CoordinatorMessage savedMessage = messageRepository.save(message);

        updateActivityScore(registrationCode);

        return savedMessage;
    }

    private void updateActivityScore(String registrationCode) {
//        CommunityActivityScore score = scoreRepository.findByRegistrationCode(registrationCode)
//                .orElseGet(() -> {
//                    CommunityActivityScore newScore = new CommunityActivityScore();
//                    //newScore.setRegistrationCode(registrationCode);
//                    return newScore;
//                });

//        score.incrementCoordinatorMessage();
//        scoreRepository.save(score);
    }

    public List<CoordinatorMessage> getMessagesByRegistrationCode(String registrationCode) {
        return messageRepository.findByRegistrationCode(registrationCode);
    }

    public List<CoordinatorMessage> getAllMessages() {
        return messageRepository.findAll();
    }
}