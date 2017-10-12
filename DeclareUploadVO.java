/*
 * Copyright 2013-2015 duolabao.com All right reserved. This software is the
 * confidential and proprietary information of duolabao.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with duolabao.com.
 */
package com.duolabao.base.upload;

/**
 * 类DeclareUploadVO的实现描述：TODO 类实现描述
 *
 * @author chengdong.wei 2016年09月24日 16:57:48
 */
public class DeclareUploadVO {

    /**
     * 附件
     */
    private byte[]            file;
    /**
     * 商户编号
     */
    private String            customerNum;
    /**
     * 附件类型 GENERAL(普通), LICENCE(执照), PROTOCAL(协议), IDENTITYFRONT(身份正面), IDENTITYOPPOSITE(身份反面), IDENTITYDOOR(省份证门头照),
     * BANKFRONT(银行卡正面), BANKOPPOSITE(银行卡反面), PERMIT(开户许可证), AUTHORIZATION(开户许可证), 店铺(SHOP), DOOR
     */
    private String            attachType;

    /**
     * @return the $name
     */
    public byte[] getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(byte[] file) {
        this.file = file;
    }

    /**
     * @return the $name
     */
    public String getCustomerNum() {
        return customerNum;
    }

    /**
     * @param customerNum the customerNum to set
     */
    public void setCustomerNum(String customerNum) {
        this.customerNum = customerNum;
    }

    /**
     * @return the $name
     */
    public String getAttachType() {
        return attachType;
    }

    /**
     * @param attachType the attachType to set
     */
    public void setAttachType(String attachType) {
        this.attachType = attachType;
    }
}
