package org.ivipi.ratel;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitScan;
import lombok.extern.slf4j.Slf4j;
import org.ivipi.ratel.common.model.Result;
import org.ivipi.ratel.rockie.client.api.RockieApi;
import org.ivipi.ratel.rockie.common.model.Content;
import org.ivipi.ratel.rockie.common.model.ContentAdd;
import org.ivipi.ratel.rockie.common.model.Document;
import org.ivipi.ratel.rockie.common.model.DocumentAccess;
import org.ivipi.ratel.rockie.common.model.DocumentAccessAdd;
import org.ivipi.ratel.rockie.common.model.DocumentAccessDelete;
import org.ivipi.ratel.rockie.common.model.DocumentAccessPage;
import org.ivipi.ratel.rockie.common.model.DocumentAccessUpdate;
import org.ivipi.ratel.rockie.common.model.DocumentAdd;
import org.ivipi.ratel.rockie.common.model.DocumentPage;
import org.ivipi.ratel.rockie.common.model.Folder;
import org.ivipi.ratel.rockie.common.model.FolderAdd;
import org.ivipi.ratel.rockie.common.model.FolderDelete;
import org.ivipi.ratel.rockie.common.model.FolderPage;
import org.ivipi.ratel.rockie.common.model.FolderUpdate;
import org.ivipi.ratel.system.client.api.SystemApi;
import org.ivipi.ratel.system.client.api.TokenSignService;
import org.ivipi.ratel.system.common.model.CustomerAdd;
import org.ivipi.ratel.system.common.model.CustomerInfo;
import org.ivipi.ratel.system.common.model.CustomerPassword;
import org.ivipi.ratel.system.common.model.CustomerUpdate;
import org.ivipi.ratel.system.common.model.License;
import org.ivipi.ratel.system.common.model.LicenseAdd;
import org.ivipi.ratel.system.common.model.LicensePage;
import org.ivipi.ratel.system.common.model.Login;
import org.ivipi.ratel.system.common.model.Order;
import org.ivipi.ratel.system.common.model.Product;
import org.ivipi.ratel.system.common.model.ProductAdd;
import org.ivipi.ratel.system.common.model.ProductDelete;
import org.ivipi.ratel.system.common.model.ProductPage;
import org.ivipi.ratel.system.common.model.ProductUpdate;
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
@RetrofitScan("org.ivipi.ratel")
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

    @BeforeAll
    public void beforeAll(){
        log.info("Start test now ...");
        String name = generateName();
        String password = generatePassword();

        register(name, password);
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
        Result result = systemApi.logout();
        assertNotNull(result);
        assertTrue(result.isSuccess());

    }

    @AfterEach
    public void afterEach(){

    }

    private void login(String name, String password) {
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
        CustomerAdd customerAdd = new CustomerAdd();
        customerAdd.setCustomerName(name);
        customerAdd.setPassword(password);
        Result result = systemApi.register(customerAdd);
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }

    private String generatePassword() {
        return "Password1";
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
}
