package com.sample.profileservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ProfileServiceApplication.class)
@AutoConfigureMockMvc
abstract class AbstractRestTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    ResultActions doPost(String uri, Object dto) throws Exception {
        return mvc.perform(post(uri).content(asString(dto)).contentType(APPLICATION_JSON));
    }

    <T> T doPostAndReturn(String uri, Object dto, ResultMatcher status, Class<T> type) throws Exception {
        MvcResult result = doPost(uri, dto)
                .andExpect(status)
                .andReturn();
        return fromJson(result.getResponse().getContentAsString(), type);
    }

    <T> T doGetAndReturn(String uri, Class<T> type) throws Exception {
        return doGetAndReturn(uri, status().isOk(), type);
    }

    ResultActions doGet(String uri) throws Exception {
        return mvc.perform(get(uri).contentType(APPLICATION_JSON));
    }

    <T> T doGetAndReturn(String uri, ResultMatcher status, Class<T> type) throws Exception {
        MvcResult result = doGet(uri)
                .andExpect(status)
                .andReturn();
        return fromJson(result.getResponse().getContentAsString(), type);
    }

    <T> T doGetAndReturnList(String uri, ResultMatcher status, Class<T> type) throws Exception {
        MvcResult result = doGet(uri)
                .andExpect(status)
                .andReturn();
        return fromJsonToList(result.getResponse().getContentAsString(), type);
    }

    private <T> T fromJson(String data, Class<T> type) throws IOException {
        return mapper.readValue(data, type);
    }

    private <T> T fromJsonToList(String data, Class<T> type) throws IOException {
        return (T) mapper.readValue(data, new TypeReference<List<T>>() {});
    }

    private String asString(final Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] longToByte(Long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }


}
