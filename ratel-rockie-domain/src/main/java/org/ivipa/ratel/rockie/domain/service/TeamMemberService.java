package org.ivipa.ratel.rockie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.ivipa.ratel.rockie.common.model.TeamMember;
import org.ivipa.ratel.rockie.common.model.TeamMemberAdd;
import org.ivipa.ratel.rockie.common.model.TeamMemberDelete;
import org.ivipa.ratel.rockie.common.model.TeamMemberPage;
import org.ivipa.ratel.rockie.common.model.TeamMemberQuery;
import org.ivipa.ratel.rockie.common.model.TeamMemberUpdate;
import org.ivipa.ratel.rockie.common.utils.RockieError;
import org.ivipa.ratel.rockie.domain.entity.TeamMemberDo;
import org.ivipa.ratel.rockie.domain.mapper.TeamMemberMapper;
import org.ivipa.ratel.system.common.model.Auth;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TeamMemberService extends ServiceImpl<TeamMemberMapper, TeamMemberDo> {

    private final static int MAX_PAGE_SIZE = 99999999;

    @Autowired
    private ContentService contentService;

    public Page<TeamMember> getTeamMembers(Auth auth, TeamMemberPage teamMemberPage) {
        Page<TeamMember> page = new Page<>(teamMemberPage.getPageNum(), teamMemberPage.getPageSize());
        List<TeamMember> result = baseMapper.getTeamMembers(page, teamMemberPage.getTeamId());
        return page.setRecords(result);
    }

    public Page<TeamMember> getTeamMembersByTeamId(Long teamId) {
        Page<TeamMemberDo> page = new Page<>(1, MAX_PAGE_SIZE);
        QueryWrapper<TeamMemberDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("team_id", teamId);
        Page<TeamMemberDo> result = baseMapper.selectPage(page, queryWrapper);
        Page<TeamMember> teamMemberPage = new Page<>(1, MAX_PAGE_SIZE);
        teamMemberPage.setRecords(convertTeamMemberDos(result.getRecords()));
        return teamMemberPage;
    }


    public Page<TeamMember> getTeamMembersByCustomerId(Long customerId) {
        Page<TeamMemberDo> page = new Page<>(1, MAX_PAGE_SIZE);
        QueryWrapper<TeamMemberDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("customer_id", customerId);
        Page<TeamMemberDo> result = baseMapper.selectPage(page, queryWrapper);
        Page<TeamMember> teamMemberPage = new Page<>(1, MAX_PAGE_SIZE);
        teamMemberPage.setRecords(convertTeamMemberDos(result.getRecords()));
        return teamMemberPage;
    }

    public TeamMember getTeamMember(Auth auth, TeamMemberQuery teamMemberQuery) {
        QueryWrapper<TeamMemberDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("customer_id", teamMemberQuery.getCustomerId());
        queryWrapper.eq("team_id", teamMemberQuery.getTeamId());
        TeamMemberDo teamMemberDo = this.getOne(queryWrapper);
        if (teamMemberDo != null) {
            TeamMember teamMember = convertTeamMemberDo(teamMemberDo);
            return teamMember;
        } else {
            return null;
        }
    }

    public TeamMember addTeamMember(Auth auth, TeamMemberAdd teamMemberAdd) {
        Page<TeamMember> teamMembers;

        teamMembers = getTeamMembersByTeamId(teamMemberAdd.getTeamId());
        teamMembers.getRecords().forEach(teamMember -> {
            if (teamMember.getCustomerId().equals(teamMemberAdd.getCustomerId())) {
                throw RockieError.TEAM_MEMBER_TEAM_MEMBER_EXISTS.newException();
            }
        });
        TeamMemberDo teamMemberDo = convertTeamMemberAdd(teamMemberAdd);
        teamMemberDo.setCustomerId(teamMemberAdd.getCustomerId());
        teamMemberDo.setTeamId(teamMemberAdd.getTeamId());
        teamMemberDo.setMemberType(teamMemberAdd.getMemberType());
        teamMemberDo.setCreatedDate(LocalDateTime.now());
        teamMemberDo.setUpdatedDate(LocalDateTime.now());
        save(teamMemberDo);
        TeamMember teamMember = convertTeamMemberDo(teamMemberDo);
        return teamMember;
    }

    public TeamMember updateTeamMember(Auth auth, TeamMemberUpdate teamMemberUpdate) {
        if (teamMemberUpdate.getTeamId() == null || teamMemberUpdate.getCustomerId() == null || teamMemberUpdate.getMemberType() == null) {
            throw RockieError.TEAM_MEMBER_INVALID_TEAM_MEMBER_REQUEST.newException();
        }
        TeamMemberQuery teamMemberQuery = new TeamMemberQuery();
        teamMemberQuery.setCustomerId(teamMemberUpdate.getCustomerId());
        teamMemberQuery.setTeamId(teamMemberUpdate.getTeamId());
        TeamMember oldTeamMember = getTeamMember(auth, teamMemberQuery);
        if (oldTeamMember == null) {
            throw RockieError.TEAM_MEMBER_TEAM_MEMBER_NOT_FOUND.newException();
        }

        oldTeamMember.setMemberType(teamMemberUpdate.getMemberType());
        oldTeamMember.setUpdatedDate(LocalDateTime.now());
        baseMapper.updateTeamMember(oldTeamMember);

        TeamMember teamMember = getTeamMember(auth, teamMemberQuery);
        return teamMember;
    }


    public boolean deleteTeamMember(Auth auth, TeamMemberDelete teamMemberDelete) {
        if (teamMemberDelete.getTeamId() == null || teamMemberDelete.getCustomerId() == null) {
            throw RockieError.TEAM_MEMBER_INVALID_TEAM_MEMBER_REQUEST.newException();
        }
        TeamMemberQuery teamMemberQuery = new TeamMemberQuery();
        teamMemberQuery.setCustomerId(teamMemberDelete.getCustomerId());
        teamMemberQuery.setTeamId(teamMemberDelete.getTeamId());
        TeamMember oldTeamMember = getTeamMember(auth, teamMemberQuery);
        if (oldTeamMember == null) {
            throw RockieError.TEAM_MEMBER_TEAM_MEMBER_NOT_FOUND.newException();
        }
        QueryWrapper<TeamMemberDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("customer_id", teamMemberDelete.getCustomerId());
        queryWrapper.eq("team_id", teamMemberDelete.getTeamId());
        return this.remove(queryWrapper);
    }

    private List<TeamMember> convertTeamMemberDos(List<TeamMemberDo> teamMemberDos) {
        List<TeamMember> teamMembers = new ArrayList<>();
        teamMemberDos.forEach(
                teamMemberDo -> {
                    TeamMember teamMember = new TeamMember();
                    BeanUtils.copyProperties(teamMemberDo, teamMember);
                    teamMembers.add(teamMember);
                }
        );
        return teamMembers;
    }

    private TeamMember convertTeamMemberDo(TeamMemberDo teamMemberDo) {
        TeamMember teamMember = new TeamMember();
        BeanUtils.copyProperties(teamMemberDo, teamMember);
        return teamMember;
    }

    private TeamMemberDo convertTeamMember(TeamMember teamMember) {
        TeamMemberDo teamMemberDo = new TeamMemberDo();
        BeanUtils.copyProperties(teamMember, teamMemberDo);
        return teamMemberDo;
    }

    private TeamMemberDo convertTeamMemberUpdate(TeamMemberUpdate teamMemberUpdate, TeamMemberDo oldTeamMemberDo) {
        BeanUtils.copyProperties(teamMemberUpdate, oldTeamMemberDo);
        return oldTeamMemberDo;
    }

    private TeamMemberDo convertTeamMemberAdd(TeamMemberAdd teamMemberAdd) {
        TeamMemberDo teamMemberDo = new TeamMemberDo();
        BeanUtils.copyProperties(teamMemberAdd, teamMemberDo);
        return teamMemberDo;
    }
}
