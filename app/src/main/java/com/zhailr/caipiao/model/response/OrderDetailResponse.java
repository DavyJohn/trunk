package com.zhailr.caipiao.model.response;

import java.util.List;

/**
 * Created by zhailiangrong on 16/7/25.
 */
public class OrderDetailResponse {


    /**
     * code : 200
     * data : {"append":"1","ball_num":"02,13,15,23,24,29|06","bet_amount":"10","cancel_time":"","contentMap":["5001%05,11,12,16,17,31|05_1","5001%04,11,12,16,26,30|12_1","5001%12,15,18,20,29,30|10_1","5001%14,20,22,23,28,29|02_1","5001%03,06,09,13,17,18|05_1"],"create_time":"2016-08-02 16:09:50","is_append":"0","issue_num":"2016089","lotterystatus":"3","multiple":"1","node_count":"5","orderId":"20160802160950000412","pay_amount":"10","pay_time":"2016-08-02 16:09:53","pay_way":"0","play_type":"单式","siteId":"1","status":"1","type_code":"双色球","win_amount":""}
     * message : 成功
     */

    private String code;
    /**
     * append : 1
     * ball_num : 02,13,15,23,24,29|06
     * bet_amount : 10
     * cancel_time :
     * contentMap : ["5001%05,11,12,16,17,31|05_1","5001%04,11,12,16,26,30|12_1","5001%12,15,18,20,29,30|10_1","5001%14,20,22,23,28,29|02_1","5001%03,06,09,13,17,18|05_1"]
     * create_time : 2016-08-02 16:09:50
     * is_append : 0
     * issue_num : 2016089
     * lotterystatus : 3
     * multiple : 1
     * node_count : 5
     * orderId : 20160802160950000412
     * pay_amount : 10
     * pay_time : 2016-08-02 16:09:53
     * pay_way : 0
     * play_type : 单式
     * siteId : 1
     * status : 1
     * type_code : 双色球
     * win_amount :
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
//        private String append;
        private String ball_num;
        private String bet_amount;
        private String cancel_time;
        private String create_time;
        private String is_append;
        private String issue_num;
//        private String lotterystatus;
        private String multiple;
        private String node_count;
        private String orderId;
        private String pay_amount;
        private String pay_time;
        private String pay_way;
        private String play_type;
        private String siteId;
        private String status;
        private String type_code;
        private String win_amount;
        private List<OrderInfoBean> orderInfo;//订单信息
        private List<TicketinfoBean> ticketinfo;//彩票信息
        private List<ChaseInfo> myChaseInfo;

        public List<ChaseInfo> getMyChaseInfo() {
            return myChaseInfo;
        }

        public void setMyChaseInfo(List<ChaseInfo> myChaseInfo) {
            this.myChaseInfo = myChaseInfo;
        }

        public List<ChaseInfo> getChaseinfo() {
            return myChaseInfo;
        }

        public void setChaseinfo(List<ChaseInfo> chaseinfo) {
            this.myChaseInfo = chaseinfo;
        }

        private List<String> contentMap;

//        public String getAppend() {
//            return append;
//        }
//
//        public void setAppend(String append) {
//            this.append = append;
//        }

        public String getBall_num() {
            return ball_num;
        }

        public void setBall_num(String ball_num) {
            this.ball_num = ball_num;
        }

        public String getBet_amount() {
            return bet_amount;
        }

        public void setBet_amount(String bet_amount) {
            this.bet_amount = bet_amount;
        }

        public String getCancel_time() {
            return cancel_time;
        }

        public void setCancel_time(String cancel_time) {
            this.cancel_time = cancel_time;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getIs_append() {
            return is_append;
        }

        public void setIs_append(String is_append) {
            this.is_append = is_append;
        }

        public String getIssue_num() {
            return issue_num;
        }

        public void setIssue_num(String issue_num) {
            this.issue_num = issue_num;
        }

//        public String getLotterystatus() {
//            return lotterystatus;
//        }
//
//        public void setLotterystatus(String lotterystatus) {
//            this.lotterystatus = lotterystatus;
//        }

        public String getMultiple() {
            return multiple;
        }

        public void setMultiple(String multiple) {
            this.multiple = multiple;
        }

        public String getNode_count() {
            return node_count;
        }

        public void setNode_count(String node_count) {
            this.node_count = node_count;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getPay_amount() {
            return pay_amount;
        }

        public void setPay_amount(String pay_amount) {
            this.pay_amount = pay_amount;
        }

        public String getPay_time() {
            return pay_time;
        }

        public void setPay_time(String pay_time) {
            this.pay_time = pay_time;
        }

        public String getPay_way() {
            return pay_way;
        }

        public void setPay_way(String pay_way) {
            this.pay_way = pay_way;
        }

        public String getPlay_type() {
            return play_type;
        }

        public void setPlay_type(String play_type) {
            this.play_type = play_type;
        }

        public String getSiteId() {
            return siteId;
        }

        public void setSiteId(String siteId) {
            this.siteId = siteId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getType_code() {
            return type_code;
        }

        public void setType_code(String type_code) {
            this.type_code = type_code;
        }

        public String getWin_amount() {
            return win_amount;
        }

        public void setWin_amount(String win_amount) {
            this.win_amount = win_amount;
        }

        public List<OrderInfoBean> getOrderInfo() {
            return orderInfo;
        }

        public void setOrderInfo(List<OrderInfoBean> orderInfo) {
            this.orderInfo = orderInfo;
        }

        public List<TicketinfoBean> getTicketinfo() {
            return ticketinfo;
        }

        public void setTicketinfo(List<TicketinfoBean> ticketinfo) {
            this.ticketinfo = ticketinfo;
        }

        public List<String> getContentMap() {
            return contentMap;
        }

        public void setContentMap(List<String> contentMap) {
            this.contentMap = contentMap;
        }


        //追号信息
        public static class ChaseInfo{
            private String amount;
            private String content;
            private String issue_num;
            private String multiple;
            private String plan_type;
            private String play_type;
            private String status;

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getIssue_num() {
                return issue_num;
            }

            public void setIssue_num(String issue_num) {
                this.issue_num = issue_num;
            }

            public String getMultiple() {
                return multiple;
            }

            public void setMultiple(String multiple) {
                this.multiple = multiple;
            }

            public String getPlan_type() {
                return plan_type;
            }

            public void setPlan_type(String plan_type) {
                this.plan_type = plan_type;
            }

            public String getPlay_type() {
                return play_type;
            }

            public void setPlay_type(String play_type) {
                this.play_type = play_type;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }


        public static class OrderInfoBean{
            private String content;
            private String multiple;
            private String play_way;

            public String getPlay_way() {
                return play_way;
            }

            public void setPlay_way(String play_way) {
                this.play_way = play_way;
            }

            public String getMultiple() {

                return multiple;
            }

            public void setMultiple(String multiple) {
                this.multiple = multiple;
            }

            public void setContent(String content) {

                this.content = content;
            }

            public String getContent() {

                return content;
            }
        }
        public static class TicketinfoBean{
            private String take_ticket_way;

            public String getTake_ticket_way() {
                return take_ticket_way;
            }

            public void setTake_ticket_way(String take_ticket_way) {
                this.take_ticket_way = take_ticket_way;
            }

            private String amount;
            private String append;
            private String content;
            private String is_append;
            private String issue_num;
            private String multiple;
            private String node;
            private String open_time;
            private String play_way;
            private String print_time;
            private String prize;
            private String result;
            private String settlement_time;
            private String siteId;

            public String getTicketId() {
                return ticketId;
            }

            public void setTicketId(String ticketId) {
                this.ticketId = ticketId;
            }

            private String ticketId;
            private String status;
            private String ticket_no;
            private String type_code;
            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getAppend() {

                return append;
            }

            public void setAppend(String append) {
                this.append = append;
            }

            public String getAmount() {

                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getType_code() {
                return type_code;
            }

            public void setType_code(String type_code) {
                this.type_code = type_code;
            }

            public String getTicket_no() {

                return ticket_no;
            }

            public void setTicket_no(String ticket_no) {
                this.ticket_no = ticket_no;
            }

            public String getStatus() {

                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getSiteId() {

                return siteId;
            }

            public void setSiteId(String siteId) {
                this.siteId = siteId;
            }

            public String getSettlement_time() {

                return settlement_time;
            }

            public void setSettlement_time(String settlement_time) {
                this.settlement_time = settlement_time;
            }

            public String getResult() {

                return result;
            }

            public void setResult(String result) {
                this.result = result;
            }

            public String getPrize() {

                return prize;
            }

            public void setPrize(String prize) {
                this.prize = prize;
            }

            public String getPrint_time() {

                return print_time;
            }

            public void setPrint_time(String print_time) {
                this.print_time = print_time;
            }

            public String getPlay_way() {

                return play_way;
            }

            public void setPlay_way(String play_way) {
                this.play_way = play_way;
            }

            public String getOpen_time() {

                return open_time;
            }

            public void setOpen_time(String open_time) {
                this.open_time = open_time;
            }

            public String getNode() {

                return node;
            }

            public void setNode(String node) {
                this.node = node;
            }

            public String getMultiple() {

                return multiple;
            }

            public void setMultiple(String multiple) {
                this.multiple = multiple;
            }

            public String getIssue_num() {

                return issue_num;
            }

            public void setIssue_num(String issue_num) {
                this.issue_num = issue_num;
            }

            public String getIs_append() {

                return is_append;
            }

            public void setIs_append(String is_append) {
                this.is_append = is_append;
            }
        }
    }
}
