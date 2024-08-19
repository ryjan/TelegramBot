package org.ryjan.telegram.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.aiplatform.v1.*;
import com.google.cloud.aiplatform.v1.schema.predict.prediction.TextExtractionPredictionResult;
import com.google.cloud.aiplatform.v1.schema.predict.prediction.TextSentimentPredictionResult;
import com.google.cloud.aiplatform.v1beta1.PredictResponse;

import org.ryjan.telegram.config.BotConfig;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.cloud.aiplatform.v1beta1.PredictionServiceClient;
import com.google.cloud.aiplatform.v1beta1.PredictionServiceSettings;
import com.google.protobuf.Value;
import com.google.protobuf.util.JsonFormat;

import java.io.IOException;
import java.util.*;

@Service
public class ChatGPTService { // ВЪЕБАНА ПАРА ДНЕЙ ВПУСТУЮ, НУЖНЫ ЕБУЧИЕ ДЕНЬГИ
    private final String PROJECT_ID = BotConfig.CHATGPT_API_URL;
    private final String LOCATION = "us-central1";
    private final String MODEL_ID = "gemini-flash";
    private final String PUBLISHER = "google";

    private final String API_KEY = BotConfig.CHATGPT_API_TOKEN;

    public String askQuestion(String question) throws IOException {
        String endpoint = String.format("%s-aiplatform.googleapis.com:443", LOCATION);
        PredictionServiceSettings predictionServiceSettings = PredictionServiceSettings.newBuilder()
                .setEndpoint(endpoint)
                .build();

        try (PredictionServiceClient predictionServiceClient = PredictionServiceClient.create(predictionServiceSettings)) {
            Content content = Content.newBuilder()
                    .addParts(Part.newBuilder().setText(question).build())
                    .build();
            GenerateContentRequest request = GenerateContentRequest.newBuilder()
                    .setModel(MODEL_ID)
                    .setContents(0, content)
                    .build();
            /*String modelFullId = String.format("projects/%s/locations/%s/publishers/%s/models/%s",
                    PROJECT_ID, LOCATION, PUBLISHER, MODEL_ID);
            EndpointName endpointName = EndpointName.of(PROJECT_ID, LOCATION, MODEL_ID);

            String publisher = "google";
            String model = MODEL_ID;

            Value.Builder instanceValue = Value.newBuilder();
            JsonFormat.parser().merge(String.format("{\"content\": \"%s\"}", question), instanceValue);

            Value.Builder parametersValue = Value.newBuilder();
            JsonFormat.parser().merge("{\"temperature\": 0.2, \"maxOutputTokens\": 256, \"topP\": 0.8, \"topK\": 40}", parametersValue);

            PredictResponse response = predictionServiceClient.predict(
                    modelFullId,
                    List.of(instanceValue.build()),
                    parametersValue.build()
            );

            return response.getPredictions(0).getStructValue().getFieldsOrThrow("content").getStringValue(); */
        }
        return null;
    }
}
