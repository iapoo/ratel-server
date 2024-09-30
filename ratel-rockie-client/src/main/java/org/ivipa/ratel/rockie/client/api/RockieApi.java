package org.ivipa.ratel.rockie.client.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitClient;
import com.github.lianjiatech.retrofit.spring.boot.interceptor.Intercept;
import org.ivipa.ratel.common.model.Result;
import org.ivipa.ratel.rockie.common.model.Document;
import org.ivipa.ratel.rockie.common.model.DocumentAccess;
import org.ivipa.ratel.rockie.common.model.DocumentAccessAdd;
import org.ivipa.ratel.rockie.common.model.DocumentAccessDelete;
import org.ivipa.ratel.rockie.common.model.DocumentAccessPage;
import org.ivipa.ratel.rockie.common.model.DocumentAccessUpdate;
import org.ivipa.ratel.rockie.common.model.DocumentAdd;
import org.ivipa.ratel.rockie.common.model.DocumentDelete;
import org.ivipa.ratel.rockie.common.model.DocumentPage;
import org.ivipa.ratel.rockie.common.model.DocumentUpdate;
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
import org.ivipa.ratel.system.client.api.TokenSignInterceptor;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.List;

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
    Result<Page<Document>> getDocuments(@Body DocumentPage documentPage);

    @POST("document/add")
    Result addDocument(@Body DocumentAdd documentAdd);

    @POST("document/update")
    Result updateDocument(@Body DocumentUpdate documentUpdate);

    @POST("document/delete")
    Result deleteDocument(@Body DocumentDelete documentDelete);

    @POST("document-access/document-accesses")
    Result<Page<DocumentAccess>> getDocumentAccesses(@Body DocumentAccessPage documentAccessPage);

    @POST("document-access/add")
    Result<List<DocumentAccess>> addDocumentAccesses(@Body DocumentAccessAdd documentAccessAdd);

    @POST("document-access/update")
    Result<List<DocumentAccess>> updateDocumentAccesses(@Body DocumentAccessUpdate documentAccessUpdate);

    @POST("document-access/delete")
    Result<Boolean> deleteDocumentAccesses(@Body DocumentAccessDelete documentAccessDelete);


    @POST("team/teams")
    public Result<Page<Team>> getTeams(@Body TeamPage teamPage);
    @POST("team/team")
    public Result<Team> getTeam(@Body TeamQuery teamQuery);
    @POST("team/add")
    public Result<Team> addTeam(@Body TeamAdd teamAdd);
    @POST("team/update")
    public Result updateTeam(@Body TeamUpdate teamUpdate);
    @POST("team/delete")
    public Result deleteTeams(@Body TeamDelete teamDelete);

    @POST("teamMember/teamMembers")
    public Result<Page<TeamMember>> getTeamMembers(@Body TeamMemberPage teamMemberPage);
    @POST("teamMember/teamMember")
    public Result<TeamMember> getTeamMember(@Body TeamMemberQuery teamMemberQuery);
    @POST("teamMember/add")
    public Result<TeamMember> addTeamMember(@Body TeamMemberAdd teamMemberAdd);
    @POST("teamMember/update")
    public Result updateTeamMember(@Body TeamMemberUpdate teamMemberUpdate);
    @POST("teamMember/delete")
    public Result deleteTeamMembers(@Body TeamMemberDelete teamMemberDelete);
    @POST("operator/operators")
    public Result<Page<Operator>> getOperators(@Body OperatorPage operatorPage);
    @POST("operator/operator")
    public Result<Operator> getOperator(@Body OperatorQuery operatorQuery);
    @POST("operator/add")
    public Result<Operator> addOperator(@Body OperatorAdd operatorAdd);
    @POST("operator/update")
    public Result updateOperator(@Body OperatorUpdate operatorUpdate);
    @POST("operator/delete")
    public Result deleteOperators(@Body OperatorDelete operatorDelete);
}

