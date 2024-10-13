package org.ivipa.ratel;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitScan;
import lombok.extern.slf4j.Slf4j;
import org.ivipa.ratel.common.model.Result;
import org.ivipa.ratel.rockie.client.api.RockieApi;
import org.ivipa.ratel.rockie.common.model.ContentAdd;
import org.ivipa.ratel.rockie.common.model.Document;
import org.ivipa.ratel.rockie.common.model.DocumentAccess;
import org.ivipa.ratel.rockie.common.model.DocumentAccessAdd;
import org.ivipa.ratel.rockie.common.model.DocumentAccessDelete;
import org.ivipa.ratel.rockie.common.model.DocumentAccessPage;
import org.ivipa.ratel.rockie.common.model.DocumentAccessUpdate;
import org.ivipa.ratel.rockie.common.model.DocumentAdd;
import org.ivipa.ratel.rockie.common.model.DocumentPage;
import org.ivipa.ratel.rockie.common.model.Folder;
import org.ivipa.ratel.rockie.common.model.FolderAdd;
import org.ivipa.ratel.rockie.common.model.FolderDelete;
import org.ivipa.ratel.rockie.common.model.FolderPage;
import org.ivipa.ratel.rockie.common.model.FolderUpdate;
import org.ivipa.ratel.rockie.common.model.OperatorDocument;
import org.ivipa.ratel.rockie.common.model.OperatorDocumentPage;
import org.ivipa.ratel.rockie.common.model.Team;
import org.ivipa.ratel.rockie.common.model.TeamAdd;
import org.ivipa.ratel.rockie.common.model.TeamDelete;
import org.ivipa.ratel.rockie.common.model.TeamMember;
import org.ivipa.ratel.rockie.common.model.TeamMemberAdd;
import org.ivipa.ratel.rockie.common.model.TeamMemberDelete;
import org.ivipa.ratel.rockie.common.model.TeamMemberPage;
import org.ivipa.ratel.rockie.common.model.TeamMemberQuery;
import org.ivipa.ratel.rockie.common.model.TeamMemberUpdate;
import org.ivipa.ratel.rockie.common.model.TeamPage;
import org.ivipa.ratel.rockie.common.model.TeamQuery;
import org.ivipa.ratel.rockie.common.model.TeamUpdate;
import org.ivipa.ratel.system.client.api.SystemApi;
import org.ivipa.ratel.system.client.api.TokenSignService;
import org.ivipa.ratel.system.common.model.CustomerAdd;
import org.ivipa.ratel.system.common.model.CustomerInfo;
import org.ivipa.ratel.system.common.model.License;
import org.ivipa.ratel.system.common.model.LicenseAdd;
import org.ivipa.ratel.system.common.model.LicensePage;
import org.ivipa.ratel.system.common.model.Login;
import org.ivipa.ratel.system.common.model.Product;
import org.ivipa.ratel.system.common.model.ProductAdd;
import org.ivipa.ratel.system.common.model.ProductPage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@RetrofitScan("org.ivipa.ratel")
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RockieClientOperatorTest {

    @Autowired
    private SystemApi systemApi;

    @Autowired
    private RockieApi rockieApi;

    @Autowired
    private TokenSignService tokenSignService;

    private CustomerInfo testCustomerInfo;
    private Product testProduct;
    private License testLicense;
    private Folder testFolder;
    private boolean enableLogout;
    private String adminName = "admin";
    private String adminPassword = "97b6a7c35da84e9073ea511a4eb2bc72a5edf5c733a010b406baa02fa3386149d04fccfcf61a4885e4e09d0c1001b1eb78ee86ffea97e57f8e61dae4f97b7170";

    @BeforeAll
    public void beforeAll(){

    }

    @BeforeEach
    public  void beforeEach(){
        log.info("Start Operator Test now ... {}");
        String name = generateName();
        String password = generatePassword();

        enableLogout = false;
        register(name, password);
        enableLogout = true;
        login(name, password, false);
        name = adminName;
        password = adminPassword;
        login(name, password, true);
        //Sleep so different customer name can be generated
        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
            //Nothing needed here
        }
    }

    @AfterAll
    public void afterAll(){
        log.info("End Operation Test now ...");
        if(enableLogout) {
            Result result = systemApi.logout();
            assertNotNull(result);
            assertTrue(result.isSuccess());
        }

    }

    @AfterEach
    public void afterEach(){

    }

    private void login(String name, String password, boolean isAdmin) {
        log.info("Login is started");
        Login login = new Login();
        login.setName(name);
        login.setPassword(password);
        Result result = systemApi.login(login);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        String token = result.getData().toString();
        tokenSignService.setToken(token);
        Result<CustomerInfo> customerInfoResult = systemApi.info();
        assertNotNull(result);
        assertTrue(result.isSuccess());
        if(!isAdmin) {
            testCustomerInfo = customerInfoResult.getData();
        } else {
            Result adminResult = systemApi.admin();
            assertNotNull(adminResult);
            assertTrue(adminResult.isSuccess());
        }

    }

    private String getTimeString() {
        String timeString;
        timeString = DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss");
        return timeString + RandomUtil.randomInt(1, 9999999);
    }

    private String generateName() {
        String namePrefix = "Customer";
        String nameSuffix = DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss");
        String name = namePrefix + nameSuffix;
        return name;
    }

    private void register(String name, String password) {
        log.info("Register is started");
        CustomerAdd customerAdd = new CustomerAdd();
        customerAdd.setCustomerName(name);
        customerAdd.setPassword(password);
        customerAdd.setEmail(name + "@test.com");
        Result result = systemApi.register(customerAdd);
//        String token = result.getData().toString();
//        log.info("Token is setup with value={}", token);
//        tokenSignService.setToken(token);
        assertNotNull(result);
        log.info("===={}", result.getMessage());
        assertTrue(result.isSuccess());
    }

    private String generatePassword() {
        Digester sha512 = new Digester(DigestAlgorithm.SHA512);
        return sha512.digestHex("Password1");
    }

    private void addDocument(String documentName, String contentName, String content, boolean expectedResult) {
        ContentAdd contentAdd = new ContentAdd();
        contentAdd.setContentName(contentName);
        contentAdd.setContent(content);
        DocumentAdd documentAdd = new DocumentAdd();
        documentAdd.setDocumentName(documentName);
        documentAdd.setContent(contentAdd);
        Result result = rockieApi.addDocument(documentAdd);
        assertNotNull(result);
        if (expectedResult) {
            assertTrue(result.isSuccess());
        } else {
            assertFalse(result.isSuccess());
        }
    }

    private Document checkDocument(String documentName) {
        DocumentPage documentPage = new DocumentPage();
        documentPage.setPageSize(99999);
        Result<Page<Document>> documentResult = rockieApi.getDocuments(documentPage);
        assertNotNull(documentResult);
        assertTrue(documentResult.isSuccess());
        long size = documentResult.getData().getRecords().size();
        boolean checkDocument = false;
        Document document = null;
        for (long i = size - 1; i >= 0; i--) {
            Document theDocument = documentResult.getData().getRecords().get((int) i);
            if (theDocument.getDocumentName().equals(documentName)) {
                checkDocument = true;
                document = theDocument;
            }
        }
        assertTrue(checkDocument);
        return document;
    }

    @Test
    public void testGetOperatorDocuments() {
        log.info("Test Get Operator documents");
        String testDocumentName = "Test Get Operator Documents" + getTimeString();
        String testContentName = "Test Content Name";
        String testContent = "Test Content of document";
        addDocument(testDocumentName, testContentName, testContent, true);
        Document document = checkDocument(testDocumentName);
        assertNotNull(document);
        OperatorDocumentPage operatorDocumentPage = new OperatorDocumentPage();
        Result<Page<OperatorDocument>> pageResult = rockieApi.getOperatorDocuments(operatorDocumentPage);
        assertNotNull(pageResult);
        assertTrue(pageResult.isSuccess());
        assertTrue(pageResult.getData().getRecords().size() > 0);
    }

}
