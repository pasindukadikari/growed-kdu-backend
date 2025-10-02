package lk.kdu.growed.backend.dto;

public class CreateSessionRequest {
    private String nickname;
    private String indexNumber;
    
    public CreateSessionRequest() {}
    
    public String getNickname() { 
        return nickname; 
    }
    
    public void setNickname(String nickname) { 
        this.nickname = nickname; 
    }
    
    public String getIndexNumber() { 
        return indexNumber; 
    }
    
    public void setIndexNumber(String indexNumber) { 
        this.indexNumber = indexNumber; 
    }
    
    @Override
    public String toString() {
        return "CreateSessionRequest{" +
                "nickname='" + nickname + '\'' +
                ", indexNumber='" + indexNumber + '\'' +
                '}';
    }
}

