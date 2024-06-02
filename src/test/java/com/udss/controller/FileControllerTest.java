package com.udss.controller;

import com.udss.bean.FileResponse;
import com.udss.service.S3Service;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class FileControllerTest {

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private FileController fileController;

    private MockMvc mockMvc;

    @Test
    public void testSearchFiles() {
        String username = "sandy";
        String searchTerm = "log";
        List<FileResponse> expectedFiles = Arrays.asList(new FileResponse("sandy/logistics.txt", null), new FileResponse( "jai/logistics.txt", null));

        when(s3Service.searchFiles(username, searchTerm)).thenReturn(expectedFiles);

        List<FileResponse> result = fileController.searchFiles(username, searchTerm);
        assertEquals(expectedFiles, result);
    }

    @Test
    public void testUploadFile() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(fileController).build();
        String username = "sandy";
        MockMultipartFile file = new MockMultipartFile(
                "file", "logistics-test.txt", MediaType.TEXT_PLAIN_VALUE, "file content".getBytes());

        mockMvc.perform(multipart("/files/upload/" + username).file(file))
                .andExpect(status().isOk());
    }
}

