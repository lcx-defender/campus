package com.lcx.campus.controller;

import com.lcx.campus.domain.User;
import com.lcx.campus.domain.dto.StudentUser;
import com.lcx.campus.domain.vo.Result;
import com.lcx.campus.enums.UserType;
import com.lcx.campus.repository.FileRepository;
import com.lcx.campus.service.IDeptService;
import com.lcx.campus.service.IStudentService;
import com.lcx.campus.utils.FileUploadUtils;
import com.lcx.campus.utils.MimeTypeUtils;
import com.lcx.campus.utils.SecurityUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;

import static org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor.FILTER_EXPRESSION;

/**
 * 智能问答客服
 *
 * @author 刘传星
 * @since 2025-05-18
 */
@Slf4j
@RestController
@RequestMapping("/ai-chat")
public class CustomerServiceController {
    @jakarta.annotation.Resource
    private FileRepository fileRepository;
    @jakarta.annotation.Resource
    private VectorStore vectorStore;
    @jakarta.annotation.Resource
    private ChatClient serviceChatClient;
    @Resource
    private IDeptService deptService;
    @Resource
    private FileStorageService fileStorageService;

    @RequestMapping(value = "/service", produces = "text/html;charset=utf-8")
    public Flux<String> chat(String prompt) {
        // 根据用户需求，获取对应部门的文件
        /*Long deptId = deptService.getUniversityIdByUserId(SecurityUtils.getLoginUser().getUserId());
        org.springframework.core.io.Resource file = fileRepository.getFile(deptId);
        if (!file.exists()) {
            throw new RuntimeException("对应部门文件不存在！");
        }*/

        // 请求模型
        return serviceChatClient.prompt()
                .user(prompt)
//                .advisors(a -> a.param(FILTER_EXPRESSION, "file_name == '" + file.getFilename() + "'"))
                .advisors(a -> a.param(FILTER_EXPRESSION, true)) // 不限制文件范围
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
            boolean success = fileRepository.save(deptId, file.getResource()); // 本地保存
            if (!success) {
                return Result.fail("保存文件失败！");
            }
//            String fileUrl = FileUploadUtils.upload("knowledge-base/", file, MimeTypeUtils.PDF, fileStorageService); // oss上传

            // 3.写入向量库
            this.writeToVectorStore(file);
            return Result.success();
        } catch (Exception e) {
            log.error("Failed to upload PDF.", e);
            return Result.fail("上传文件失败！");
        }
    }

    private void writeToVectorStore(MultipartFile file) {
        // 1.创建PDF的读取器
        PagePdfDocumentReader reader = new PagePdfDocumentReader(
                file.getResource(), // 文件源
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
