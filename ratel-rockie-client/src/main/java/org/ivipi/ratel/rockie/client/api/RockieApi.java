package org.ivipi.ratel.rockie.client.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.lianjiatech.retrofit.spring.boot.annotation.Intercept;
import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import org.ivipi.ratel.common.model.Result;
import org.ivipi.ratel.rockie.common.model.Document;
import org.ivipi.ratel.rockie.common.model.DocumentAdd;
import org.ivipi.ratel.rockie.common.model.DocumentDelete;
import org.ivipi.ratel.rockie.common.model.DocumentPage;
import org.ivipi.ratel.rockie.common.model.DocumentUpdate;
import org.ivipi.ratel.rockie.common.model.Folder;
import org.ivipi.ratel.rockie.common.model.FolderAdd;
import org.ivipi.ratel.rockie.common.model.FolderDelete;
import org.ivipi.ratel.rockie.common.model.FolderPage;
import org.ivipi.ratel.rockie.common.model.FolderUpdate;
import org.ivipi.ratel.system.client.api.TokenSignInterceptor;
import retrofit2.http.Body;
import retrofit2.http.POST;

@RetrofitClient(baseUrl = "${retrofit.rockie.baseUrl}")
@Intercept(handler = TokenSignInterceptor.class, include = {"/**"})
public interface RockieApi {

    @POST("folder/folders")
    Result<Page<Folder>> getFolders(@Body FolderPage folderPage);

    @POST("folder/add")
    Result addFolder(@Body  FolderAdd folderAdd);

    @POST("folder/update")
    Result updateFolder(@Body FolderUpdate folderUpdate);

    @POST("folder/delete")
    Result deleteFolder(@Body FolderDelete folderDelete);

    @POST("document/documents")
    Result<Page<Document>> getDocument(@Body DocumentPage documentPage);

    @POST("document/add")
    Result addDocument(@Body DocumentAdd documentAdd);

    @POST("document/update")
    Result updateDocument(@Body DocumentUpdate documentUpdate);

    @POST("document/delete")
    Result deleteDocument(@Body DocumentDelete documentDelete);

}
