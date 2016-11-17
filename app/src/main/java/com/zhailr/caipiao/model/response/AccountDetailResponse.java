package com.zhailr.caipiao.model.response;

import java.util.List;

/**
 * Created by zhailiangrong on 16/7/22.
 */
public class AccountDetailResponse {

    /**
     * code : 200
     * data : {"detailList":[{"amount":"2","category":"投注","orderId":"20160720123920000064","tradeTime":"2016-07-20 12:39:22"},{"amount":"2","category":"投注","orderId":"20160721150353000066","tradeTime":"2016-07-21 15:03:55"},{"amount":"2","category":"投注","orderId":"20160721150458000067","tradeTime":"2016-07-21 15:04:59"},{"amount":"2","category":"投注","orderId":"20160721150523000068","tradeTime":"2016-07-21 15:05:25"},{"amount":"2","category":"投注","orderId":"20160721150614000069","tradeTime":"2016-07-21 15:06:23"},{"amount":"2","category":"投注","orderId":"20160721150700000070","tradeTime":"2016-07-21 15:07:01"},{"amount":"2","category":"投注","orderId":"20160721150723000071","tradeTime":"2016-07-21 15:07:25"},{"amount":"2","category":"投注","orderId":"20160721150918000072","tradeTime":"2016-07-21 15:09:19"},{"amount":"2","category":"投注","orderId":"20160721151131000074","tradeTime":"2016-07-21 15:11:32"},{"amount":"2","category":"投注","orderId":"20160722123807000165","tradeTime":"2016-07-22 12:38:14"}],"totalCount":11}
     * message : 成功
     */

    private String code;
    /**
     * detailList : [{"amount":"2","category":"投注","orderId":"20160720123920000064","tradeTime":"2016-07-20 12:39:22"},{"amount":"2","category":"投注","orderId":"20160721150353000066","tradeTime":"2016-07-21 15:03:55"},{"amount":"2","category":"投注","orderId":"20160721150458000067","tradeTime":"2016-07-21 15:04:59"},{"amount":"2","category":"投注","orderId":"20160721150523000068","tradeTime":"2016-07-21 15:05:25"},{"amount":"2","category":"投注","orderId":"20160721150614000069","tradeTime":"2016-07-21 15:06:23"},{"amount":"2","category":"投注","orderId":"20160721150700000070","tradeTime":"2016-07-21 15:07:01"},{"amount":"2","category":"投注","orderId":"20160721150723000071","tradeTime":"2016-07-21 15:07:25"},{"amount":"2","category":"投注","orderId":"20160721150918000072","tradeTime":"2016-07-21 15:09:19"},{"amount":"2","category":"投注","orderId":"20160721151131000074","tradeTime":"2016-07-21 15:11:32"},{"amount":"2","category":"投注","orderId":"20160722123807000165","tradeTime":"2016-07-22 12:38:14"}]
     * totalCount : 11
     */

    private DataBean data;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        private String totalCount;
        /**
         * amount : 2
         * category : 投注
         * orderId : 20160720123920000064
         * tradeTime : 2016-07-20 12:39:22
         */

        private List<DetailListBean> detailList;

        public String getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(String totalCount) {
            this.totalCount = totalCount;
        }

        public List<DetailListBean> getDetailList() {
            return detailList;
        }

        public void setDetailList(List<DetailListBean> detailList) {
            this.detailList = detailList;
        }

        public static class DetailListBean {
            private String amount;
            private String category;
            private String orderId;
            private String tradeTime;

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public String getTradeTime() {
                return tradeTime;
            }

            public void setTradeTime(String tradeTime) {
                this.tradeTime = tradeTime;
            }
        }
    }
}
