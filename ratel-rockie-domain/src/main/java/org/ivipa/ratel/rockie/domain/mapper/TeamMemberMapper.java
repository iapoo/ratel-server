package org.ivipa.ratel.rockie.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.ivipa.ratel.rockie.common.model.TeamMember;
import org.ivipa.ratel.rockie.common.model.TeamMemberDetail;
import org.ivipa.ratel.rockie.domain.entity.TeamMemberDo;

import java.util.List;

public interface TeamMemberMapper extends BaseMapper<TeamMemberDo> {
    List<TeamMember> getTeamMembers(IPage<TeamMember> page, @Param("teamId") Long teamId);
    List<TeamMemberDetail> getTeamMemberDetails(IPage<TeamMemberDetail> page, @Param("teamId") Long teamId, @Param("like") String like);
    List<TeamMember> getTeamMembersByCustomerId(IPage<TeamMember> page, @Param("customerId") Long customerId);
    void updateTeamMember(@Param("teamMember") TeamMember teamMember);
}
