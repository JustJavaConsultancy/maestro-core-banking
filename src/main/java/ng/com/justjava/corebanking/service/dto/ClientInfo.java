package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "id",
    "bank_cbn_code",
    "bank_name",
    "console_url",
    "js_background_image",
    "css_url",
    "logo_url",
    "footer_text",
    "show_options_icon",
    "paginate",
    "paginate_count",
    "options",
    "merchant",
    "colors",
    "meta"
})
@Generated("jsonschema2pojo")
public class ClientInfo {

    @JsonProperty("name")
    private Object name;
    @JsonProperty("id")
    private Object id;
    @JsonProperty("bank_cbn_code")
    private Object bankCbnCode;
    @JsonProperty("bank_name")
    private Object bankName;
    @JsonProperty("console_url")
    private Object consoleUrl;
    @JsonProperty("js_background_image")
    private Object jsBackgroundImage;
    @JsonProperty("css_url")
    private Object cssUrl;
    @JsonProperty("logo_url")
    private Object logoUrl;
    @JsonProperty("footer_text")
    private Object footerText;
    @JsonProperty("show_options_icon")
    private Boolean showOptionsIcon;
    @JsonProperty("paginate")
    private Boolean paginate;
    @JsonProperty("paginate_count")
    private Integer paginateCount;
    @JsonProperty("options")
    private Object options;
    @JsonProperty("merchant")
    private Object merchant;
    @JsonProperty("colors")
    private Object colors;
    @JsonProperty("meta")
    private Object meta;

    @JsonProperty("name")
    public Object getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(Object name) {
        this.name = name;
    }

    @JsonProperty("id")
    public Object getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Object id) {
        this.id = id;
    }

    @JsonProperty("bank_cbn_code")
    public Object getBankCbnCode() {
        return bankCbnCode;
    }

    @JsonProperty("bank_cbn_code")
    public void setBankCbnCode(Object bankCbnCode) {
        this.bankCbnCode = bankCbnCode;
    }

    @JsonProperty("bank_name")
    public Object getBankName() {
        return bankName;
    }

    @JsonProperty("bank_name")
    public void setBankName(Object bankName) {
        this.bankName = bankName;
    }

    @JsonProperty("console_url")
    public Object getConsoleUrl() {
        return consoleUrl;
    }

    @JsonProperty("console_url")
    public void setConsoleUrl(Object consoleUrl) {
        this.consoleUrl = consoleUrl;
    }

    @JsonProperty("js_background_image")
    public Object getJsBackgroundImage() {
        return jsBackgroundImage;
    }

    @JsonProperty("js_background_image")
    public void setJsBackgroundImage(Object jsBackgroundImage) {
        this.jsBackgroundImage = jsBackgroundImage;
    }

    @JsonProperty("css_url")
    public Object getCssUrl() {
        return cssUrl;
    }

    @JsonProperty("css_url")
    public void setCssUrl(Object cssUrl) {
        this.cssUrl = cssUrl;
    }

    @JsonProperty("logo_url")
    public Object getLogoUrl() {
        return logoUrl;
    }

    @JsonProperty("logo_url")
    public void setLogoUrl(Object logoUrl) {
        this.logoUrl = logoUrl;
    }

    @JsonProperty("footer_text")
    public Object getFooterText() {
        return footerText;
    }

    @JsonProperty("footer_text")
    public void setFooterText(Object footerText) {
        this.footerText = footerText;
    }

    @JsonProperty("show_options_icon")
    public Boolean getShowOptionsIcon() {
        return showOptionsIcon;
    }

    @JsonProperty("show_options_icon")
    public void setShowOptionsIcon(Boolean showOptionsIcon) {
        this.showOptionsIcon = showOptionsIcon;
    }

    @JsonProperty("paginate")
    public Boolean getPaginate() {
        return paginate;
    }

    @JsonProperty("paginate")
    public void setPaginate(Boolean paginate) {
        this.paginate = paginate;
    }

    @JsonProperty("paginate_count")
    public Integer getPaginateCount() {
        return paginateCount;
    }

    @JsonProperty("paginate_count")
    public void setPaginateCount(Integer paginateCount) {
        this.paginateCount = paginateCount;
    }

    @JsonProperty("options")
    public Object getOptions() {
        return options;
    }

    @JsonProperty("options")
    public void setOptions(Object options) {
        this.options = options;
    }

    @JsonProperty("merchant")
    public Object getMerchant() {
        return merchant;
    }

    @JsonProperty("merchant")
    public void setMerchant(Object merchant) {
        this.merchant = merchant;
    }

    @JsonProperty("colors")
    public Object getColors() {
        return colors;
    }

    @JsonProperty("colors")
    public void setColors(Object colors) {
        this.colors = colors;
    }

    @JsonProperty("meta")
    public Object getMeta() {
        return meta;
    }

    @JsonProperty("meta")
    public void setMeta(Object meta) {
        this.meta = meta;
    }

    @Override
    public String toString() {
        return "ClientInfo{" +
            "name=" + name +
            ", id=" + id +
            ", bankCbnCode=" + bankCbnCode +
            ", bankName=" + bankName +
            ", consoleUrl=" + consoleUrl +
            ", jsBackgroundImage=" + jsBackgroundImage +
            ", cssUrl=" + cssUrl +
            ", logoUrl=" + logoUrl +
            ", footerText=" + footerText +
            ", showOptionsIcon=" + showOptionsIcon +
            ", paginate=" + paginate +
            ", paginateCount=" + paginateCount +
            ", options=" + options +
            ", merchant=" + merchant +
            ", colors=" + colors +
            ", meta=" + meta +
            '}';
    }
}
