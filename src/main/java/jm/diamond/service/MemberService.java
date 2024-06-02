package jm.diamond.service;

import jm.diamond.dao.entity.User;

public interface MemberService {

   User doWork();

   void registerMember(User user);
}
