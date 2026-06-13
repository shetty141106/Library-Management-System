package com.project.lms.Entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Authentication {

  @Id
  private int loginId;
  private int password;

  @OneToOne
  private Staff staff;



}
