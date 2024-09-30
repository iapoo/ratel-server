package org.ivipa.ratel;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
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
import org.ivipa.ratel.rockie.common.model.Operator;
import org.ivipa.ratel.rockie.common.model.OperatorAdd;
import org.ivipa.ratel.rockie.common.model.OperatorDelete;
import org.ivipa.ratel.rockie.common.model.OperatorPage;
import org.ivipa.ratel.rockie.common.model.OperatorQuery;
import org.ivipa.ratel.rockie.common.model.OperatorUpdate;
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
public class RockieClientApplicationTest {

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

    @BeforeAll
    public void beforeAll(){
        log.info("Start test now ...");
        String name = generateName();
        String password = generatePassword();

        enableLogout = false;
        register(name, password);
        enableLogout = true;
        login(name, password);
        addTestProduct();
        addTestLicense();
        addTestFolder();
    }

    @BeforeEach
    public  void beforeEach(){
//        try {
//            Thread.sleep(60L);
//        } catch (InterruptedException e) {
//            //Nothing needed here
//        }
    }

    @AfterAll
    public void afterAll(){
        log.info("End test now ...");
        if(enableLogout) {
            Result result = systemApi.logout();
            assertNotNull(result);
            assertTrue(result.isSuccess());
        }

    }

    @AfterEach
    public void afterEach(){

    }

    private void login(String name, String password) {
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
        testCustomerInfo = customerInfoResult.getData();
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
        assertTrue(result.isSuccess());
    }

    private String generatePassword() {
        Digester sha512 = new Digester(DigestAlgorithm.SHA512);
        return sha512.digestHex("Password1");
    }

    private void addTestProduct() {
        String productName = "Product" + getTimeString();
        addProduct(productName, true);
        testProduct = checkProduct(productName);
    }

    private void addTestLicense() {
        addLicense(testProduct.getProductId(), true);
        testLicense = checkLicense(testProduct.getProductId());
    }

    private void addTestFolder() {
        String folderName = "Folder" + getTimeString();
        addFolder(folderName, null, true);
        testFolder = checkFolder(folderName, null);
    }

    private void addProduct(String productName, boolean expectedResult) {
        ProductAdd productAdd = new ProductAdd();
        productAdd.setProductName(productName);
        Result result = systemApi.addProduct(productAdd);
        assertNotNull(result);
        if (expectedResult) {
            assertTrue(result.isSuccess());
        } else {
            assertFalse(result.isSuccess());
        }
    }

    private Product checkProduct(String productName) {
        ProductPage productPage = new ProductPage();
        productPage.setPageSize(99999);
        Result<Page<Product>> productResult = systemApi.getProducts(productPage);
        assertNotNull(productResult);
        assertTrue(productResult.isSuccess());
        long size = productResult.getData().getRecords().size();
        boolean checkProduct = false;
        Product product = null;
        for (long i = size - 1; i >= 0; i--) {
            Product theProduct = productResult.getData().getRecords().get((int) i);
            if (theProduct.getProductName().equals(productName)) {
                checkProduct = true;
                product = theProduct;
            }
        }
        assertTrue(checkProduct);
        return product;
    }

    private void addLicense(Long productId, boolean expectedResult) {
        LicenseAdd licenseAdd = new LicenseAdd();
        licenseAdd.setProductId(productId);
        licenseAdd.setRemark("Add License Test");
        Result result = systemApi.addLicense(licenseAdd);
        assertNotNull(result);
        if (expectedResult) {
            assertTrue(result.isSuccess());
        } else {
            assertFalse(result.isSuccess());
        }
    }

    private License checkLicense(Long productId) {
        LicensePage licensePage = new LicensePage();
        licensePage.setPageSize(99999);
        Result<Page<License>> licenseResult = systemApi.getLicenses(licensePage);
        long size = licenseResult.getData().getRecords().size();
        boolean checkLicense = false;
        License license = null;
        for (long i = size - 1; i >= 0; i--) {
            License theLicense = licenseResult.getData().getRecords().get((int) i);
            if (theLicense.getProductId().equals(productId) && theLicense.getCustomerId().equals(testCustomerInfo.getCustomerId())) {
                checkLicense = true;
                license = theLicense;
                break;
            }
        }
        assertTrue(checkLicense);
        return license;
    }


    private void addFolder(String folderName, Long parentFolderId, boolean expectedResult) {
        FolderAdd folderAdd = new FolderAdd();
        folderAdd.setFolderName(folderName);
        folderAdd.setParentId(parentFolderId);
        Result result = rockieApi.addFolder(folderAdd);
        assertNotNull(result);
        if (expectedResult) {
            assertTrue(result.isSuccess());
        } else {
            assertFalse(result.isSuccess());
        }
    }

    private Folder checkFolder(String folderName, Long parentId) {
        FolderPage folderPage = new FolderPage();
        folderPage.setPageSize(99999);
        folderPage.setParentId(parentId);
        Result<Page<Folder>> folderResult = rockieApi.getFolders(folderPage);
        assertNotNull(folderResult);
        assertTrue(folderResult.isSuccess());
        int folderCount = folderResult.getData().getRecords().size();
        Folder folder = null;
        for(int i = 0; i< folderCount; i ++) {
            Folder theFolder = folderResult.getData().getRecords().get(i);
            if(folderName.equals(theFolder.getFolderName())) {
                folder = theFolder;
                break;
            }
        }
        assertNotNull(folder);
        return folder;
    }

    @Test
    public void testAddFolder() {
       log.info("Add Folder with new folder name");
       String testFolderName = "Test Add Folder" + getTimeString();
       addFolder(testFolderName,  null,true);
       Folder folder = checkFolder(testFolderName, null);
       assertNotNull(folder);
       log.info("Add folder with existing folder name");
       addFolder(testFolderName, null,false);
    }

    @Test
    public void testAddSubFolder() {
        log.info("Add Sub Folder with new folder name");
        String testFolderName = "Test Sub Add Folder" + getTimeString();
        addFolder(testFolderName, null,true);
        Folder folder = checkFolder(testFolderName, null);
        assertNotNull(folder);
        testFolderName = "Test Add Child Folder" + getTimeString();
        addFolder(testFolderName, folder.getFolderId(),true);
        Folder subFolder = checkFolder(testFolderName, folder.getFolderId());
        assertNotNull(folder);
    }

    @Test
    public void testDeleteFolder() {
        log.info("Test delete folder");
        String testFolderName = "Test Delete Folder" + getTimeString();
        addFolder(testFolderName, null,true);
        Folder folder = checkFolder(testFolderName, null);
        assertNotNull(folder);
        FolderDelete folderDelete = new FolderDelete();
        folderDelete.setFolderId(folder.getFolderId());
        Result deleteResult = rockieApi.deleteFolder(folderDelete);
        assertNotNull(deleteResult);
        assertTrue(deleteResult.isSuccess());
    }

    @Test
    public void testUpdateFolder() {
        log.info("Test update folder");
        String testFolderName = "Test Update Folder" + getTimeString();
        addFolder(testFolderName, null,true);
        Folder folder = checkFolder(testFolderName, null);
        assertNotNull(folder);
        FolderUpdate folderUpdate = new FolderUpdate();
        folderUpdate.setFolderId(folder.getFolderId());
        folderUpdate.setFolderName(testFolderName + " is updated");
        Result updateResult = rockieApi.updateFolder(folderUpdate);
        assertNotNull(updateResult);
        assertTrue(updateResult.isSuccess());
    }

    @Test
    public void testGetFolders() {
        log.info("Test get folder");
        String testFolderName = "Test Get Folder" + getTimeString();
        addFolder(testFolderName, null,true);
        Folder folder = checkFolder(testFolderName, null);
        assertNotNull(folder);
    }

    private void addDocument(String documentName, String contentName, String content, boolean expectedResult) {
        ContentAdd contentAdd = new ContentAdd();
        contentAdd.setContentName(contentName);
        contentAdd.setContent(content);
        DocumentAdd documentAdd = new DocumentAdd();
        documentAdd.setDocumentName(documentName);
        documentAdd.setContent(contentAdd);
        documentAdd.setFolderId(testFolder.getFolderId());
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
        documentPage.setFolderId(testFolder.getFolderId());
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
    public void testAddDocument() {
        log.info("Add document with new document name");
        String testDocumentName = "Test Add Document" + getTimeString();
        String testContentName = "Test Content Name";
        String testContent = "Test Content of document";
        addDocument(testDocumentName, testContentName, testContent, true);
        Document document = checkDocument(testDocumentName);
        assertNotNull(document);
        log.info("Add document with existing document name");
        addDocument(testDocumentName, testContentName, testContent, false);
    }

    private List<DocumentAccess> addDocumentAccesses() {
      return null;
    }

    @Test
    public void testAddDocumentAccesses() {
        log.info("Test Add document access");
        String testDocumentName = "DocumentAccessTest" + getTimeString();
        String testContentName = "DocumentAccessTest Content Name";
        String testContent = "DocumentAccessTest Content";
        addDocument(testDocumentName, testContentName, testContent, true);
        Document document = checkDocument(testDocumentName);
        assertNotNull(document);
        DocumentAccessAdd documentAccessAdd = new DocumentAccessAdd();
        documentAccessAdd.setDocumentId(document.getDocumentId());
        documentAccessAdd.setAccessMode(1L);
        Long customerIds[] = {1L, 2L, 3L};
        documentAccessAdd.setCustomerIds(customerIds);
        Result<List<DocumentAccess>> result = rockieApi.addDocumentAccesses(documentAccessAdd);
        assertNotNull(result);
        assertNotNull(result.getData().size() == 3);
    }

    @Test
    public void testGetDocumentAccesses() {
        log.info("Test Get document access");
        String testDocumentName = "DocumentAccessTest" + getTimeString();
        String testContentName = "DocumentAccessTest Content Name";
        String testContent = "DocumentAccessTest Content";
        addDocument(testDocumentName, testContentName, testContent, true);
        Document document = checkDocument(testDocumentName);
        assertNotNull(document);
        DocumentAccessAdd documentAccessAdd = new DocumentAccessAdd();
        documentAccessAdd.setDocumentId(document.getDocumentId());
        documentAccessAdd.setAccessMode(1L);
        Long customerIds[] = {1L, 2L, 3L};
        documentAccessAdd.setCustomerIds(customerIds);
        Result<List<DocumentAccess>> addResult = rockieApi.addDocumentAccesses(documentAccessAdd);
        assertNotNull(addResult);
        assertNotNull(addResult.getData().size() == 3);
        DocumentAccessPage  documentAccessPage = new DocumentAccessPage();
        documentAccessPage.setDocumentId(document.getDocumentId());
        Result<Page<DocumentAccess>> getResult = rockieApi.getDocumentAccesses(documentAccessPage);
        assertNotNull(getResult);
        assertNotNull(getResult.getData().getSize() == 3);
    }
    @Test
    public void testUpdateDocumentAccesses() {
        log.info("Test Update document access");
        String testDocumentName = "DocumentAccessTest" + getTimeString();
        String testContentName = "DocumentAccessTest Content Name";
        String testContent = "DocumentAccessTest Content";
        addDocument(testDocumentName, testContentName, testContent, true);
        Document document = checkDocument(testDocumentName);
        assertNotNull(document);
        DocumentAccessAdd documentAccessAdd = new DocumentAccessAdd();
        documentAccessAdd.setDocumentId(document.getDocumentId());
        documentAccessAdd.setAccessMode(1L);
        Long customerIds[] = {1L, 2L, 3L};
        documentAccessAdd.setCustomerIds(customerIds);
        Result<List<DocumentAccess>> addResult = rockieApi.addDocumentAccesses(documentAccessAdd);
        assertNotNull(addResult);
        assertTrue(addResult.getData().size() == 3);
        DocumentAccessUpdate documentAccessUpdate = new DocumentAccessUpdate();
        documentAccessUpdate.setDocumentId(document.getDocumentId());
        documentAccessUpdate.setAccessMode(0L);
        Long updateCustomerIds[] = {2L, 3L, 4L};
        documentAccessUpdate.setCustomerIds(customerIds);
        Result<List<DocumentAccess>> updateResult = rockieApi.updateDocumentAccesses(documentAccessUpdate);
        assertNotNull(updateResult);
        assertTrue(updateResult.getData().size() == 3);
        assertTrue(updateResult.getData().get(0).getAccessMode().longValue() == 0L);
    }


    @Test
    public void testDeleteDocumentAccesses() {
        log.info("Test Delete document access");
        String testDocumentName = "DocumentAccessTest" + getTimeString();
        String testContentName = "DocumentAccessTest Content Name";
        String testContent = "DocumentAccessTest Content";
        addDocument(testDocumentName, testContentName, testContent, true);
        Document document = checkDocument(testDocumentName);
        assertNotNull(document);
        DocumentAccessAdd documentAccessAdd = new DocumentAccessAdd();
        documentAccessAdd.setDocumentId(document.getDocumentId());
        documentAccessAdd.setAccessMode(1L);
        Long customerIds[] = {1L, 2L, 3L};
        documentAccessAdd.setCustomerIds(customerIds);
        Result<List<DocumentAccess>> addResult = rockieApi.addDocumentAccesses(documentAccessAdd);
        assertNotNull(addResult);
        assertTrue(addResult.getData().size() == 3);
        DocumentAccessDelete documentAccessDelete = new DocumentAccessDelete();
        documentAccessDelete.setDocumentId(document.getDocumentId());
        Result<Boolean> deleteResult = rockieApi.deleteDocumentAccesses(documentAccessDelete);
        assertNotNull(deleteResult);
        assertTrue(deleteResult.getData().equals(Boolean.TRUE));
    }

    private void addTeam(String teamName, boolean expectedResult) {
        TeamAdd teamAdd = new TeamAdd();
        teamAdd.setTeamName(teamName);
        Result<Team> result = rockieApi.addTeam(teamAdd);
        assertNotNull(result);
        if(expectedResult) {
            assertTrue(result.isSuccess());
        } else {
            assertFalse(result.isSuccess());
        }
    }

    private Team checkTeam(String teamName) {
        TeamPage teamPage = new TeamPage();
        teamPage.setPageSize(99999);
        Result<Page<Team>> teamResult = rockieApi.getTeams(teamPage);
        assertNotNull(teamResult);
        assertTrue(teamResult.isSuccess());
        long size = teamResult.getData().getRecords().size();
        boolean checkTeam = false;
        Team team = null;
        for(long i = 0; i < size; i++) {
            Team theTeam = teamResult.getData().getRecords().get((int)i);
            if(theTeam.getTeamName().equals(teamName)) {
                checkTeam = true;
                team = theTeam;
            }
        }
        assertTrue(checkTeam);
        return team;
    }

    @Test
    public void testAddTeam() {
        log.info("Test Add Team");
        String testTeamName = "Test Add Team " + getTimeString();
        addTeam(testTeamName, true);
        Team team = checkTeam(testTeamName);
        assertNotNull(team);
    }

    @Test
    public void testGetTeam() {
        log.info("Test Get Team");
        String testTeamName = "Test Get Team " + getTimeString();
        addTeam(testTeamName, true);
        Team team = checkTeam(testTeamName);
        assertNotNull(team);
        Long teamId = team.getTeamId();
        TeamQuery teamQuery = new TeamQuery();
        teamQuery.setTeamId(teamId);
        Result<Team> result = rockieApi.getTeam(teamQuery);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        team = result.getData();
        assertNotNull(team);
        assertTrue(teamId.equals(team.getTeamId()));
    }

    @Test
    public void testGetTeams() {
        log.info("Test Get Teams");
        String testTeamName = "Test Get Teams " + getTimeString();
        addTeam(testTeamName, true);
        Team team = checkTeam(testTeamName);
        assertNotNull(team);
        Long teamId = team.getTeamId();
        TeamPage teamPage = new TeamPage();
        Result<Page<Team>> result = rockieApi.getTeams(teamPage);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        Page<Team> page = result.getData();
        assertNotNull(page);
        assertTrue(page.getSize() > 0);
    }

    @Test
    public void testUpdateTeam() {
        log.info("Test Update Team");
        String testTeamName = "Test Update Team " + getTimeString();
        String testTeamNameUpdate = "Update of Test Update Team " + getTimeString();
        addTeam(testTeamName, true);
        Team team = checkTeam(testTeamName);
        assertNotNull(team);
        Long teamId = team.getTeamId();
        TeamUpdate teamUpdate = new TeamUpdate();
        teamUpdate.setTeamId(teamId);
        teamUpdate.setTeamName(testTeamNameUpdate);
        Result updateResult = rockieApi.updateTeam(teamUpdate);
        assertNotNull(updateResult);
        assertTrue(updateResult.isSuccess());
        TeamQuery teamQuery = new TeamQuery();
        teamQuery.setTeamId(teamId);
        Result<Team> result = rockieApi.getTeam(teamQuery);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        team = result.getData();
        assertNotNull(team);
        assertTrue(teamId.equals(team.getTeamId()));
        assertTrue(testTeamNameUpdate.equals(team.getTeamName()));

    }

    @Test
    public void testDeleteTeam() {
        log.info("Test Delete Team");
        String testTeamName = "Test Delete Team " + getTimeString();
        addTeam(testTeamName, true);
        Team team = checkTeam(testTeamName);
        assertNotNull(team);
        Long teamId = team.getTeamId();
        TeamDelete teamDelete = new TeamDelete();
        teamDelete.setTeamId(teamId);
        Result deleteResult = rockieApi.deleteTeams(teamDelete);
        assertNotNull(deleteResult);
        assertTrue(deleteResult.isSuccess());
        TeamQuery teamQuery = new TeamQuery();
        teamQuery.setTeamId(teamId);
        Result<Team> result = rockieApi.getTeam(teamQuery);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        team = result.getData();
        assertTrue(team == null);
    }


    private void addOperator(Long customerId, boolean expectedResult) {
        OperatorAdd operatorAdd = new OperatorAdd();
        operatorAdd.setCustomerId(customerId);
        operatorAdd.setOperatorType(0L);
        Result<Operator> result = rockieApi.addOperator(operatorAdd);
        assertNotNull(result);
        if(expectedResult) {
            assertTrue(result.isSuccess());
        } else {
            assertFalse(result.isSuccess());
        }
    }

    private Operator checkOperator(Long customerId) {
        OperatorPage operatorPage = new OperatorPage();
        operatorPage.setPageSize(99999);
        Result<Page<Operator>> operatorResult = rockieApi.getOperators(operatorPage);
        assertNotNull(operatorResult);
        assertTrue(operatorResult.isSuccess());
        long size = operatorResult.getData().getRecords().size();
        boolean checkOperator = false;
        Operator operator = null;
        for(long i = 0; i < size; i++) {
            Operator theOperator = operatorResult.getData().getRecords().get((int)i);
            if(theOperator.getCustomerId().equals(customerId)) {
                checkOperator = true;
                operator = theOperator;
            }
        }
        assertTrue(checkOperator);
        return operator;
    }

    @Test
    public void testAddOperator() {
        log.info("Test Add Operator");
        Long customerId = testCustomerInfo.getCustomerId();
        addOperator(customerId, true);
        Operator operator = checkOperator(customerId);
        assertNotNull(operator);
    }

    @Test
    public void testGetOperator() {
        log.info("Test Get Operator");
        Long customerId = testCustomerInfo.getCustomerId();
        addOperator(customerId, true);
        Operator operator = checkOperator(customerId);
        assertNotNull(operator);
        Long operatorId = operator.getOperatorId();
        OperatorQuery operatorQuery = new OperatorQuery();
        operatorQuery.setOperatorId(operatorId);
        Result<Operator> result = rockieApi.getOperator(operatorQuery);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        operator = result.getData();
        assertNotNull(operator);
        assertTrue(operatorId.equals(operator.getOperatorId()));
    }

    @Test
    public void testGetOperators() {
        log.info("Test Get Operators");
        Long customerId = testCustomerInfo.getCustomerId();
        addOperator(customerId, true);
        Operator operator = checkOperator(customerId);
        assertNotNull(operator);
        Long operatorId = operator.getOperatorId();
        OperatorPage operatorPage = new OperatorPage();
        Result<Page<Operator>> result = rockieApi.getOperators(operatorPage);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        Page<Operator> page = result.getData();
        assertNotNull(page);
        assertTrue(page.getSize() > 0);
    }

    @Test
    public void testUpdateOperator() {
        log.info("Test Update Operator");
        Long customerId = testCustomerInfo.getCustomerId();
        addOperator(customerId, true);
        Operator operator = checkOperator(customerId);
        assertNotNull(operator);
        Long operatorId = operator.getOperatorId();
        OperatorUpdate operatorUpdate = new OperatorUpdate();
        operatorUpdate.setOperatorId(operatorId);
        operatorUpdate.setCustomerId(customerId);
        operatorUpdate.setOperatorType(2L);
        Result updateResult = rockieApi.updateOperator(operatorUpdate);
        assertNotNull(updateResult);
        assertTrue(updateResult.isSuccess());
        OperatorQuery operatorQuery = new OperatorQuery();
        operatorQuery.setOperatorId(operatorId);
        Result<Operator> result = rockieApi.getOperator(operatorQuery);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        operator = result.getData();
        assertNotNull(operator);
        assertTrue(operatorId.equals(operator.getOperatorId()));
        assertTrue(operator.getOperatorType() == 2);

    }

    @Test
    public void testDeleteOperator() {
        log.info("Test Delete Operator");
        Long customerId = testCustomerInfo.getCustomerId();
        addOperator(customerId, true);
        Operator operator = checkOperator(customerId);
        assertNotNull(operator);
        Long operatorId = operator.getOperatorId();
        OperatorDelete operatorDelete = new OperatorDelete();
        operatorDelete.setOperatorId(operatorId);
        Result deleteResult = rockieApi.deleteOperators(operatorDelete);
        assertNotNull(deleteResult);
        assertTrue(deleteResult.isSuccess());
        OperatorQuery operatorQuery = new OperatorQuery();
        operatorQuery.setOperatorId(operatorId);
        Result<Operator> result = rockieApi.getOperator(operatorQuery);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        operator = result.getData();
        assertTrue(operator == null);
    }


    private void addTeamMember(Long teamId, Long customerId, boolean expectedResult) {
        TeamMemberAdd teamMemberAdd = new TeamMemberAdd();
        teamMemberAdd.setCustomerId(customerId);
        teamMemberAdd.setTeamId(teamId);
        teamMemberAdd.setMemberType(0L);
        Result<TeamMember> result = rockieApi.addTeamMember(teamMemberAdd);
        assertNotNull(result);
        if(expectedResult) {
            assertTrue(result.isSuccess());
        } else {
            assertFalse(result.isSuccess());
        }
    }

    private TeamMember checkTeamMember(Long teamId, Long customerId) {
        TeamMemberPage teamMemberPage = new TeamMemberPage();
        teamMemberPage.setPageSize(99999);
        teamMemberPage.setTeamId(teamId);
        Result<Page<TeamMember>> teamMemberResult = rockieApi.getTeamMembers(teamMemberPage);
        assertNotNull(teamMemberResult);
        assertTrue(teamMemberResult.isSuccess());
        long size = teamMemberResult.getData().getRecords().size();
        boolean checkTeamMember = false;
        TeamMember teamMember = null;
        for(long i = 0; i < size; i++) {
            TeamMember theTeamMember = teamMemberResult.getData().getRecords().get((int)i);
            if(theTeamMember.getCustomerId().equals(customerId) && theTeamMember.getTeamId().equals(teamId)) {
                checkTeamMember = true;
                teamMember = theTeamMember;
            }
        }
        assertTrue(checkTeamMember);
        return teamMember;
    }

    @Test
    public void testAddTeamMember() {
        log.info("Test Add TeamMember");
        String testTeamName = "Test Add TeamMember " + getTimeString();
        addTeam(testTeamName, true);
        Team team = checkTeam(testTeamName);
        assertNotNull(team);
        Long customerId = testCustomerInfo.getCustomerId();
        addTeamMember(team.getTeamId(), customerId, true);
        TeamMember teamMember = checkTeamMember(team.getTeamId(), customerId);
        assertNotNull(teamMember);
    }

    @Test
    public void testGetTeamMember() {
        log.info("Test Get TeamMember");
        String testTeamName = "Test Get TeamMember " + getTimeString();
        addTeam(testTeamName, true);
        Team team = checkTeam(testTeamName);
        assertNotNull(team);
        Long customerId = testCustomerInfo.getCustomerId();
        addTeamMember(team.getTeamId(), customerId, true);
        TeamMember teamMember = checkTeamMember(team.getTeamId(), customerId);
        assertNotNull(teamMember);
        TeamMemberQuery teamMemberQuery = new TeamMemberQuery();
        teamMemberQuery.setTeamId(team.getTeamId());
        teamMemberQuery.setCustomerId(customerId);
        Result<TeamMember> result = rockieApi.getTeamMember(teamMemberQuery);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        teamMember = result.getData();
        assertNotNull(teamMember);
        assertTrue(team.getTeamId().equals(teamMember.getTeamId()));
        assertTrue(customerId.equals(teamMember.getCustomerId()));
    }

    @Test
    public void testGetTeamMembers() {
        log.info("Test Get TeamMembers");
        String testTeamName = "Test Get TeamMembers " + getTimeString();
        addTeam(testTeamName, true);
        Team team = checkTeam(testTeamName);
        assertNotNull(team);
        Long customerId = testCustomerInfo.getCustomerId();
        addTeamMember(team.getTeamId(), customerId, true);
        TeamMember teamMember = checkTeamMember(team.getTeamId(), customerId);
        assertNotNull(teamMember);
        TeamMemberPage teamMemberPage = new TeamMemberPage();
        Result<Page<TeamMember>> result = rockieApi.getTeamMembers(teamMemberPage);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        Page<TeamMember> page = result.getData();
        assertNotNull(page);
        assertTrue(page.getSize() > 0);
    }

    @Test
    public void testUpdateTeamMember() {
        log.info("Test Update TeamMember");
        String testTeamName = "Test Update TeamMember " + getTimeString();
        addTeam(testTeamName, true);
        Team team = checkTeam(testTeamName);
        assertNotNull(team);
        Long customerId = testCustomerInfo.getCustomerId();
        addTeamMember(team.getTeamId(), customerId, true);
        TeamMember teamMember = checkTeamMember(team.getTeamId(), customerId);
        assertNotNull(teamMember);
        TeamMemberUpdate teamMemberUpdate = new TeamMemberUpdate();
        teamMemberUpdate.setTeamId(team.getTeamId());
        teamMemberUpdate.setCustomerId(customerId);
        teamMemberUpdate.setMemberType(2L);
        Result updateResult = rockieApi.updateTeamMember(teamMemberUpdate);
        assertNotNull(updateResult);
        assertTrue(updateResult.isSuccess());
        TeamMemberQuery teamMemberQuery = new TeamMemberQuery();
        teamMemberQuery.setTeamId(team.getTeamId());
        teamMemberQuery.setCustomerId(customerId);
        Result<TeamMember> result = rockieApi.getTeamMember(teamMemberQuery);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        teamMember = result.getData();
        assertNotNull(teamMember);
        assertTrue(team.getTeamId().equals(teamMember.getTeamId()));
        assertTrue(customerId.equals(teamMember.getCustomerId()));
        assertTrue(teamMember.getMemberType().equals(2L));

    }

    @Test
    public void testDeleteTeamMember() {
        log.info("Test Delete TeamMember");
        String testTeamName = "Test Delete TeamMember " + getTimeString();
        addTeam(testTeamName, true);
        Team team = checkTeam(testTeamName);
        assertNotNull(team);
        Long customerId = testCustomerInfo.getCustomerId();
        addTeamMember(team.getTeamId(), customerId, true);
        TeamMember teamMember = checkTeamMember(team.getTeamId(), customerId);
        assertNotNull(teamMember);
        TeamMemberDelete teamMemberDelete = new TeamMemberDelete();
        teamMemberDelete.setTeamId(team.getTeamId());
        teamMemberDelete.setCustomerId(customerId);
        Result deleteResult = rockieApi.deleteTeamMembers(teamMemberDelete);
        assertNotNull(deleteResult);
        assertTrue(deleteResult.isSuccess());
        TeamMemberQuery teamMemberQuery = new TeamMemberQuery();
        teamMemberQuery.setTeamId(team.getTeamId());
        teamMemberQuery.setCustomerId(customerId);
        Result<TeamMember> result = rockieApi.getTeamMember(teamMemberQuery);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        teamMember = result.getData();
        assertTrue(teamMember == null);
    }

}
