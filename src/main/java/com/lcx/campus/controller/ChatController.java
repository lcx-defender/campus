package com.lcx.campus.controller;

import com.lcx.campus.domain.User;
import com.lcx.campus.domain.dto.StudentUser;
import com.lcx.campus.domain.vo.Result;
import com.lcx.campus.enums.UserType;
import com.lcx.campus.repository.ChatHistoryRepository;
import com.lcx.campus.repository.FileRepository;
import com.lcx.campus.service.IStudentService;
import com.lcx.campus.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor.FILTER_EXPRESSION;

@Slf4j
@RestController
@RequestMapping("/ai/pdf")
public class ChatController {

    @jakarta.annotation.Resource
    private FileRepository fileRepository;
    @jakarta.annotation.Resource
    private VectorStore vectorStore;
    @jakarta.annotation.Resource
    private ChatClient pdfChatClient;
    @jakarta.annotation.Resource
    private IStudentService studentService;

    @RequestMapping(value = "/chat", produces = "text/html;charset=utf-8")
    public Flux<String> chat(String prompt) {
        // 根据用户需求，获取对应部门的文件
        User user = SecurityUtils.getLoginUser().getUser();
        Long deptId = 0L;
        if(user.getUserType().equals(UserType.STUDENT.getCode())) {
            StudentUser studentUser = new StudentUser();
            studentUser.setUserId(user.getUserId());
            List<StudentUser> studentUsers = studentService.selectStudentUserList(studentUser);
            if (!studentUsers.isEmpty()) {
                deptId = studentUsers.get(0).getUniversityId();
            } else {
                Flux.error(new RuntimeException("用户类型不正确！"));
            }
        } else if(user.getUserType().equals(UserType.TEACHER.getCode())) {

        } else if(user.getUserType().equals(UserType.SYSTEM.getCode())) {
            deptId = 0L;
        } else {
            return Flux.error(new RuntimeException("用户类型不正确！"));
        }
        Resource file = fileRepository.getFile(deptId);
        if (!file.exists()) {
            // 文件不存在，不回答
            throw new RuntimeException("会话文件不存在！");
        }
        // 3.请求模型
        return pdfChatClient.prompt()
                .user(prompt)
                .advisors(a -> a.param(FILTER_EXPRESSION, "file_name == '" + file.getFilename() + "'"))
                .stream()
                .content();
    }

    /**
     * 文件上传
     */
    @RequestMapping("/upload/{deptId}")
    public Result uploadPdf(@PathVariable Long deptId, @RequestParam("file") MultipartFile file) {
        try {
            // 1. 校验文件是否为PDF格式
            if (!Objects.equals(file.getContentType(), "application/pdf")) {
                return Result.fail("只能上传PDF文件！");
            }
            // 2.保存文件
            boolean success = fileRepository.save(deptId, file.getResource());
            if (!success) {
                return Result.fail("保存文件失败！");
            }
            // 3.写入向量库
            this.writeToVectorStore(file.getResource());
            return Result.success();
        } catch (Exception e) {
            log.error("Failed to upload PDF.", e);
            return Result.fail("上传文件失败！");
        }
    }

    private void writeToVectorStore(Resource resource) {
        // 1.创建PDF的读取器
        PagePdfDocumentReader reader = new PagePdfDocumentReader(
                resource, // 文件源
                PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.defaults())
                        .withPagesPerDocument(1) // 每1页PDF作为一个Document
                        .build()
        );
        // 2.读取PDF文档，拆分为Document
        List<Document> documents = reader.read();
        // 3.写入向量库
        vectorStore.add(documents);
    }
}