package org.ivipa.ratel.rockie.server.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.ivipa.ratel.common.model.Result;
import org.ivipa.ratel.rockie.common.model.TeamMember;
import org.ivipa.ratel.rockie.common.model.TeamMemberAdd;
import org.ivipa.ratel.rockie.common.model.TeamMemberDelete;
import org.ivipa.ratel.rockie.common.model.TeamMemberPage;
import org.ivipa.ratel.rockie.common.model.TeamMemberQuery;
import org.ivipa.ratel.rockie.common.model.TeamMemberUpdate;
import org.ivipa.ratel.rockie.domain.service.TeamMemberService;
import org.ivipa.ratel.system.common.annoation.Audit;
import org.ivipa.ratel.system.common.controller.GenericController;
import org.ivipa.ratel.system.common.model.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("teamMember")
public class TeamMemberController extends GenericController {

    @Autowired
    private TeamMemberService teamMemberService;


    @PostMapping("teamMembers")
    @Audit
    public Result<Page<TeamMember>> getTeamMembers(Auth auth, @RequestBody TeamMemberPage teamMemberPage) {
        Page<TeamMember> customers = teamMemberService.getTeamMembers(auth, teamMemberPage);
        return Result.success(customers);
    }

    @PostMapping("teamMember")
    @Audit
    public Result<TeamMember> getTeamMember(Auth auth, @RequestBody TeamMemberQuery teamMemberQuery) {
        TeamMember teamMember = teamMemberService.getTeamMember(auth, teamMemberQuery);
        return Result.success(teamMember);
    }

    @PostMapping("add")
    @Audit
    public Result<TeamMember> addTeamMember(Auth auth, @RequestBody TeamMemberAdd teamMemberAdd) {
        TeamMember teamMember = teamMemberService.addTeamMember(auth, teamMemberAdd);
        return Result.success(teamMember);
    }

    @PostMapping("update")
    @Audit
    public Result updateTeamMember(Auth auth, @RequestBody TeamMemberUpdate teamMemberUpdate) {
        teamMemberService.updateTeamMember(auth, teamMemberUpdate);
        return Result.success();
    }

    @PostMapping("delete")
    @Audit
    public Result deleteTeamMembers(Auth auth, @RequestBody TeamMemberDelete teamMemberDelete) {
        teamMemberService.deleteTeamMember(auth, teamMemberDelete);
        return Result.success();
    }

}
