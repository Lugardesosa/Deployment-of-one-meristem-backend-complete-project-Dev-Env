package org.meristem.oneapp.usersservice.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.meristem.oneapp.usersservice.exception.exceptions.BadRequestException;

@AllArgsConstructor
@Getter
public enum Country {

    AFGHANISTAN("Afghanistan", "AF", "AFG", "004"),
    ALBANIA("Albania", "AL", "ALB", "008"),
    ALGERIA("Algeria", "DZ", "DZA", "012"),
    AMERICAN_SAMOA("American Samoa", "AS", "ASM", "016"),
    ANDORRA("Andorra", "AD", "AND", "020"),
    ANGOLA("Angola", "AO", "AGO", "024"),
    ANGUILLA("Anguilla", "AI", "AIA", "660"),
    ANTARCTICA("Antarctica", "AQ", "ATA", "010"),
    ANTIGUA_AND_BARBUDA("Antigua and Barbuda", "AG", "ATG", "028"),
    ARGENTINA("Argentina", "AR", "ARG", "032"),
    ARMENIA("Armenia", "AM", "ARM", "051"),
    ARUBA("Aruba", "AW", "ABW", "533"),
    AUSTRALIA("Australia", "AU", "AUS", "036"),
    AUSTRIA("Austria", "AT", "AUT", "040"),
    AZERBAIJAN("Azerbaijan", "AZ", "AZE", "031"),
    BAHAMAS("Bahamas (the)", "BS", "BHS", "044"),
    BAHRAIN("Bahrain", "BH", "BHR", "048"),
    BANGLADESH("Bangladesh", "BD", "BGD", "050"),
    BARBADOS("Barbados", "BB", "BRB", "052"),
    BELARUS("Belarus", "BY", "BLR", "112"),
    BELGIUM("Belgium", "BE", "BEL", "056"),
    BELIZE("Belize", "BZ", "BLZ", "084"),
    BENIN("Benin", "BJ", "BEN", "204"),
    BERMUDA("Bermuda", "BM", "BMU", "060"),
    BHUTAN("Bhutan", "BT", "BTN", "064"),
    BOLIVIA("Bolivia (Plurinational State of)", "BO", "BOL", "068"),
    BONAIRE_SINT_EUSTATIUS_AND_SABA("Bonaire, Sint Eustatius and Saba", "BQ", "BES", "535"),
    BOSNIA_AND_HERZEGOVINA("Bosnia and Herzegovina", "BA", "BIH", "070"),
    BOTSWANA("Botswana", "BW", "BWA", "072"),
    BOUVET_ISLAND("Bouvet Island", "BV", "BVT", "074"),
    BRAZIL("Brazil", "BR", "BRA", "076"),
    BRITISH_INDIAN_OCEAN_TERRITORY("British Indian Ocean Territory (the)", "IO", "IOT", "086"),
    BRUNEI_DARUSSALAM("Brunei Darussalam", "BN", "BRN", "096"),
    BULGARIA("Bulgaria", "BG", "BGR", "100"),
    BURKINA_FASO("Burkina Faso", "BF", "BFA", "854"),
    BURUNDI("Burundi", "BI", "BDI", "108"),
    CABO_VERDE("Cabo Verde", "CV", "CPV", "132"),
    CAMBODIA("Cambodia", "KH", "KHM", "116"),
    CAMEROON("Cameroon", "CM", "CMR", "120"),
    CANADA("Canada", "CA", "CAN", "124"),
    CAYMAN_ISLANDS("Cayman Islands (the)", "KY", "CYM", "136"),
    CENTRAL_AFRICAN_REPUBLIC("Central African Republic (the)", "CF", "CAF", "140"),
    CHAD("Chad", "TD", "TCD", "148"),
    CHILE("Chile", "CL", "CHL", "152"),
    CHINA("China", "CN", "CHN", "156"),
    CHRISTMAS_ISLAND("Christmas Island", "CX", "CXR", "162"),
    COCOS_KEELING_ISLANDS("Cocos (Keeling) Islands (the)", "CC", "CCK", "166"),
    COLOMBIA("Colombia", "CO", "COL", "170"),
    COMOROS("Comoros (the)", "KM", "COM", "174"),
    CONGO_DEMOCRATIC_REPUBLIC("Congo (the Democratic Republic of the)", "CD", "COD", "180"),
    CONGO("Congo (the)", "CG", "COG", "178"),
    COOK_ISLANDS("Cook Islands (the)", "CK", "COK", "184"),
    COSTA_RICA("Costa Rica", "CR", "CRI", "188"),
    CROATIA("Croatia", "HR", "HRV", "191"),
    CUBA("Cuba", "CU", "CUB", "192"),
    CURACAO("Curaçao", "CW", "CUW", "531"),
    CYPRUS("Cyprus", "CY", "CYP", "196"),
    CZECHIA("Czechia", "CZ", "CZE", "203"),
    COTE_DIVOIRE("Côte d'Ivoire", "CI", "CIV", "384"),
    DENMARK("Denmark", "DK", "DNK", "208"),
    DJIBOUTI("Djibouti", "DJ", "DJI", "262"),
    DOMINICA("Dominica", "DM", "DMA", "212"),
    DOMINICAN_REPUBLIC("Dominican Republic (the)", "DO", "DOM", "214"),
    ECUADOR("Ecuador", "EC", "ECU", "218"),
    EGYPT("Egypt", "EG", "EGY", "818"),
    EL_SALVADOR("El Salvador", "SV", "SLV", "222"),
    EQUATORIAL_GUINEA("Equatorial Guinea", "GQ", "GNQ", "226"),
    ERITREA("Eritrea", "ER", "ERI", "232"),
    ESTONIA("Estonia", "EE", "EST", "233"),
    ESWATINI("Eswatini", "SZ", "SWZ", "748"),
    ETHIOPIA("Ethiopia", "ET", "ETH", "231"),
    FALKLAND_ISLANDS("Falkland Islands (the) [Malvinas]", "FK", "FLK", "238"),
    FAROE_ISLANDS("Faroe Islands (the)", "FO", "FRO", "234"),
    FIJI("Fiji", "FJ", "FJI", "242"),
    FINLAND("Finland", "FI", "FIN", "246"),
    FRANCE("France", "FR", "FRA", "250"),
    FRENCH_GUIANA("French Guiana", "GF", "GUF", "254"),
    FRENCH_POLYNESIA("French Polynesia", "PF", "PYF", "258"),
    FRENCH_SOUTHERN_TERRITORIES("French Southern Territories (the)", "TF", "ATF", "260"),
    GABON("Gabon", "GA", "GAB", "266"),
    GAMBIA("Gambia (the)", "GM", "GMB", "270"),
    GEORGIA("Georgia", "GE", "GEO", "268"),
    GERMANY("Germany", "DE", "DEU", "276"),
    GHANA("Ghana", "GH", "GHA", "288"),
    GIBRALTAR("Gibraltar", "GI", "GIB", "292"),
    GREECE("Greece", "GR", "GRC", "300"),
    GREENLAND("Greenland", "GL", "GRL", "304"),
    GRENADA("Grenada", "GD", "GRD", "308"),
    GUADELOUPE("Guadeloupe", "GP", "GLP", "312"),
    GUAM("Guam", "GU", "GUM", "316"),
    GUATEMALA("Guatemala", "GT", "GTM", "320"),
    GUERNSEY("Guernsey", "GG", "GGY", "831"),
    GUINEA("Guinea", "GN", "GIN", "324"),
    GUINEA_BISSAU("Guinea-Bissau", "GW", "GNB", "624"),
    GUYANA("Guyana", "GY", "GUY", "328"),
    HAITI("Haiti", "HT", "HTI", "332"),
    HEARD_ISLAND_AND_MCDONALD_ISLANDS("Heard Island and McDonald Islands", "HM", "HMD", "334"),
    HOLY_SEE("Holy See (the)", "VA", "VAT", "336"),
    HONDURAS("Honduras", "HN", "HND", "340"),
    HONG_KONG("Hong Kong", "HK", "HKG", "344"),
    HUNGARY("Hungary", "HU", "HUN", "348"),
    ICELAND("Iceland", "IS", "ISL", "352"),
    INDIA("India", "IN", "IND", "356"),
    INDONESIA("Indonesia", "ID", "IDN", "360"),
    IRAN("Iran (Islamic Republic of)", "IR", "IRN", "364"),
    IRAQ("Iraq", "IQ", "IRQ", "368"),
    IRELAND("Ireland", "IE", "IRL", "372"),
    ISLE_OF_MAN("Isle of Man", "IM", "IMN", "833"),
    ISRAEL("Israel", "IL", "ISR", "376"),
    ITALY("Italy", "IT", "ITA", "380"),
    JAMAICA("Jamaica", "JM", "JAM", "388"),
    JAPAN("Japan", "JP", "JPN", "392"),
    JERSEY("Jersey", "JE", "JEY", "832"),
    JORDAN("Jordan", "JO", "JOR", "400"),
    KAZAKHSTAN("Kazakhstan", "KZ", "KAZ", "398"),
    KENYA("Kenya", "KE", "KEN", "404"),
    KIRIBATI("Kiribati", "KI", "KIR", "296"),
    KOREA_DEMOCRATIC_PEOPLES_REPUBLIC("Korea (the Democratic People's Republic of)", "KP", "PRK", "408"),
    KOREA_REPUBLIC("Korea (the Republic of)", "KR", "KOR", "410"),
    KUWAIT("Kuwait", "KW", "KWT", "414"),
    KYRGYZSTAN("Kyrgyzstan", "KG", "KGZ", "417"),
    LAO_PEOPLES_DEMOCRATIC_REPUBLIC("Lao People's Democratic Republic (the)", "LA", "LAO", "418"),
    LATVIA("Latvia", "LV", "LVA", "428"),
    LEBANON("Lebanon", "LB", "LBN", "422"),
    LESOTHO("Lesotho", "LS", "LSO", "426"),
    LIBERIA("Liberia", "LR", "LBR", "430"),
    LIBYA("Libya", "LY", "LBY", "434"),
    LIECHTENSTEIN("Liechtenstein", "LI", "LIE", "438"),
    LITHUANIA("Lithuania", "LT", "LTU", "440"),
    LUXEMBOURG("Luxembourg", "LU", "LUX", "442"),
    MACAO("Macao", "MO", "MAC", "446"),
    MADAGASCAR("Madagascar", "MG", "MDG", "450"),
    MALAWI("Malawi", "MW", "MWI", "454"),
    MALAYSIA("Malaysia", "MY", "MYS", "458"),
    MALDIVES("Maldives", "MV", "MDV", "462"),
    MALI("Mali", "ML", "MLI", "466"),
    MALTA("Malta", "MT", "MLT", "470"),
    MARSHALL_ISLANDS("Marshall Islands (the)", "MH", "MHL", "584"),
    MARTINIQUE("Martinique", "MQ", "MTQ", "474"),
    MAURITANIA("Mauritania", "MR", "MRT", "478"),
    MAURITIUS("Mauritius", "MU", "MUS", "480"),
    MAYOTTE("Mayotte", "YT", "MYT", "175"),
    MEXICO("Mexico", "MX", "MEX", "484"),
    MICRONESIA("Micronesia (Federated States of)", "FM", "FSM", "583"),
    MOLDOVA("Moldova (the Republic of)", "MD", "MDA", "498"),
    MONACO("Monaco", "MC", "MCO", "492"),
    MONGOLIA("Mongolia", "MN", "MNG", "496"),
    MONTENEGRO("Montenegro", "ME", "MNE", "499"),
    MONTSERRAT("Montserrat", "MS", "MSR", "500"),
    MOROCCO("Morocco", "MA", "MAR", "504"),
    MOZAMBIQUE("Mozambique", "MZ", "MOZ", "508"),
    MYANMAR("Myanmar", "MM", "MMR", "104"),
    NAMIBIA("Namibia", "NA", "NAM", "516"),
    NAURU("Nauru", "NR", "NRU", "520"),
    NEPAL("Nepal", "NP", "NPL", "524"),
    NETHERLANDS("Netherlands (the)", "NL", "NLD", "528"),
    NEW_CALEDONIA("New Caledonia", "NC", "NCL", "540"),
    NEW_ZEALAND("New Zealand", "NZ", "NZL", "554"),
    NICARAGUA("Nicaragua", "NI", "NIC", "558"),
    NIGER("Niger (the)", "NE", "NER", "562"),
    NIGERIA("Nigeria", "NG", "NGA", "566"),
    NIUE("Niue", "NU", "NIU", "570"),
    NORFOLK_ISLAND("Norfolk Island", "NF", "NFK", "574"),
    NORTHERN_MARIANA_ISLANDS("Northern Mariana Islands (the)", "MP", "MNP", "580"),
    NORWAY("Norway", "NO", "NOR", "578"),
    OMAN("Oman", "OM", "OMN", "512"),
    PAKISTAN("Pakistan", "PK", "PAK", "586"),
    PALAU("Palau", "PW", "PLW", "585"),
    PALESTINE("Palestine, State of", "PS", "PSE", "275"),
    PANAMA("Panama", "PA", "PAN", "591"),
    PAPUA_NEW_GUINEA("Papua New Guinea", "PG", "PNG", "598"),
    PARAGUAY("Paraguay", "PY", "PRY", "600"),
    PERU("Peru", "PE", "PER", "604"),
    PHILIPPINES("Philippines (the)", "PH", "PHL", "608"),
    PITCAIRN("Pitcairn", "PN", "PCN", "612"),
    POLAND("Poland", "PL", "POL", "616"),
    PORTUGAL("Portugal", "PT", "PRT", "620"),
    PUERTO_RICO("Puerto Rico", "PR", "PRI", "630"),
    QATAR("Qatar", "QA", "QAT", "634"),
    REPUBLIC_OF_NORTH_MACEDONIA("Republic of North Macedonia", "MK", "MKD", "807"),
    ROMANIA("Romania", "RO", "ROU", "642"),
    RUSSIAN_FEDERATION("Russian Federation (the)", "RU", "RUS", "643"),
    RWANDA("Rwanda", "RW", "RWA", "646"),
    REUNION("Réunion", "RE", "REU", "638"),
    SAINT_BARTHELEMY("Saint Barthélemy", "BL", "BLM", "652"),
    SAINT_HELENA_ASCENSION_AND_TRISTAN_DA_CUNHA("Saint Helena, Ascension and Tristan da Cunha", "SH", "SHN", "654"),
    SAINT_KITTS_AND_NEVIS("Saint Kitts and Nevis", "KN", "KNA", "659"),
    SAINT_LUCIA("Saint Lucia", "LC", "LCA", "662"),
    SAINT_MARTIN("Saint Martin (French part)", "MF", "MAF", "663"),
    SAINT_PIERRE_AND_MIQUELON("Saint Pierre and Miquelon", "PM", "SPM", "666"),
    SAINT_VINCENT_AND_THE_GRENADINES("Saint Vincent and the Grenadines", "VC", "VCT", "670"),
    SAMOA("Samoa", "WS", "WSM", "882"),
    SAN_MARINO("San Marino", "SM", "SMR", "674"),
    SAO_TOME_AND_PRINCIPE("Sao Tome and Principe", "ST", "STP", "678"),
    SAUDI_ARABIA("Saudi Arabia", "SA", "SAU", "682"),
    SENEGAL("Senegal", "SN", "SEN", "686"),
    SERBIA("Serbia", "RS", "SRB", "688"),
    SEYCHELLES("Seychelles", "SC", "SYC", "690"),
    SIERRA_LEONE("Sierra Leone", "SL", "SLE", "694"),
    SINGAPORE("Singapore", "SG", "SGP", "702"),
    SINT_MAARTEN("Sint Maarten (Dutch part)", "SX", "SXM", "534"),
    SLOVAKIA("Slovakia", "SK", "SVK", "703"),
    SLOVENIA("Slovenia", "SI", "SVN", "705"),
    SOLOMON_ISLANDS("Solomon Islands", "SB", "SLB", "090"),
    SOMALIA("Somalia", "SO", "SOM", "706"),
    SOUTH_AFRICA("South Africa", "ZA", "ZAF", "710"),
    SOUTH_GEORGIA_AND_THE_SOUTH_SANDWICH_ISLANDS("South Georgia and the South Sandwich Islands", "GS", "SGS", "239"),
    SOUTH_SUDAN("South Sudan", "SS", "SSD", "728"),
    SPAIN("Spain", "ES", "ESP", "724"),
    SRI_LANKA("Sri Lanka", "LK", "LKA", "144"),
    SUDAN("Sudan (the)", "SD", "SDN", "729"),
    SURINAME("Suriname", "SR", "SUR", "740"),
    SVALBARD_AND_JAN_MAYEN("Svalbard and Jan Mayen", "SJ", "SJM", "744"),
    SWEDEN("Sweden", "SE", "SWE", "752"),
    SWITZERLAND("Switzerland", "CH", "CHE", "756"),
    SYRIAN_ARAB_REPUBLIC("Syrian Arab Republic", "SY", "SYR", "760"),
    TAIWAN("Taiwan (Province of China)", "TW", "TWN", "158"),
    TAJIKISTAN("Tajikistan", "TJ", "TJK", "762"),
    TANZANIA("Tanzania, United Republic of", "TZ", "TZA", "834"),
    THAILAND("Thailand", "TH", "THA", "764"),
    TIMOR_LESTE("Timor-Leste", "TL", "TLS", "626"),
    TOGO("Togo", "TG", "TGO", "768"),
    TOKELAU("Tokelau", "TK", "TKL", "772"),
    TONGA("Tonga", "TO", "TON", "776"),
    TRINIDAD_AND_TOBAGO("Trinidad and Tobago", "TT", "TTO", "780"),
    TUNISIA("Tunisia", "TN", "TUN", "788"),
    TURKEY("Turkey", "TR", "TUR", "792"),
    TURKMENISTAN("Turkmenistan", "TM", "TKM", "795"),
    TURKS_AND_CAICOS_ISLANDS("Turks and Caicos Islands (the)", "TC", "TCA", "796"),
    TUVALU("Tuvalu", "TV", "TUV", "798"),
    UGANDA("Uganda", "UG", "UGA", "800"),
    UKRAINE("Ukraine", "UA", "UKR", "804"),
    UNITED_ARAB_EMIRATES("United Arab Emirates (the)", "AE", "ARE", "784"),
    UNITED_KINGDOM("United Kingdom of Great Britain and Northern Ireland (the)", "GB", "GBR", "826"),
    UNITED_STATES_MINOR_OUTLYING_ISLANDS("United States Minor Outlying Islands (the)", "UM", "UMI", "581"),
    UNITED_STATES_OF_AMERICA("United States of America (the)", "US", "USA", "840"),
    URUGUAY("Uruguay", "UY", "URY", "858"),
    UZBEKISTAN("Uzbekistan", "UZ", "UZB", "860"),
    VANUATU("Vanuatu", "VU", "VUT", "548"),
    VENEZUELA("Venezuela (Bolivarian Republic of)", "VE", "VEN", "862"),
    VIET_NAM("Viet Nam", "VN", "VNM", "704"),
    VIRGIN_ISLANDS_BRITISH("Virgin Islands (British)", "VG", "VGB", "092"),
    VIRGIN_ISLANDS_US("Virgin Islands (U.S.)", "VI", "VIR", "850"),
    WALLIS_AND_FUTUNA("Wallis and Futuna", "WF", "WLF", "876"),
    WESTERN_SAHARA("Western Sahara", "EH", "ESH", "732"),
    YEMEN("Yemen", "YE", "YEM", "887"),
    ZAMBIA("Zambia", "ZM", "ZMB", "894"),
    ZIMBABWE("Zimbabwe", "ZW", "ZWE", "716"),
    ALAND_ISLANDS("Åland Islands", "AX", "ALA", "248"),
    EMPTY("", "", "", "");

    private final String countryName;
    private final String alpha2;
    private final String alpha3;
    private final String numeric;


    public static Country getCountry(String value) {
        return switch (value) {
            case "Afghanistan", "AF", "AFG", "004" -> AFGHANISTAN;
            case "Albania", "AL", "ALB", "008" -> ALBANIA;
            case "Algeria", "DZ", "DZA", "012" -> ALGERIA;
            case "American Samoa", "AS", "ASM", "016" -> AMERICAN_SAMOA;
            case "Andorra", "AD", "AND", "020" -> ANDORRA;
            case "Angola", "AO", "AGO", "024" -> ANGOLA;
            case "Anguilla", "AI", "AIA", "660" -> ANGUILLA;
            case "Antarctica", "AQ", "ATA", "010" -> ANTARCTICA;
            case "Antigua and Barbuda", "AG", "ATG", "028" -> ANTIGUA_AND_BARBUDA;
            case "Argentina", "AR", "ARG", "032" -> ARGENTINA;
            case "Armenia", "AM", "ARM", "051" -> ARMENIA;
            case "Aruba", "AW", "ABW", "533" -> ARUBA;
            case "Australia", "AU", "AUS", "036" -> AUSTRALIA;
            case "Austria", "AT", "AUT", "040" -> AUSTRIA;
            case "Azerbaijan", "AZ", "AZE", "031" -> AZERBAIJAN;
            case "Bahamas (the)", "BS", "BHS", "044" -> BAHAMAS;
            case "Bahrain", "BH", "BHR", "048" -> BAHRAIN;
            case "Bangladesh", "BD", "BGD", "050" -> BANGLADESH;
            case "Barbados", "BB", "BRB", "052" -> BARBADOS;
            case "Belarus", "BY", "BLR", "112" -> BELARUS;
            case "Belgium", "BE", "BEL", "056" -> BELGIUM;
            case "Belize", "BZ", "BLZ", "084" -> BELIZE;
            case "Benin", "BJ", "BEN", "204" -> BENIN;
            case "Bermuda", "BM", "BMU", "060" -> BERMUDA;
            case "Bhutan", "BT", "BTN", "064" -> BHUTAN;
            case "Bolivia (Plurinational State of)", "BO", "BOL", "068" -> BOLIVIA;
            case "Bonaire, Sint Eustatius and Saba", "BQ", "BES", "535" -> BONAIRE_SINT_EUSTATIUS_AND_SABA;
            case "Bosnia and Herzegovina", "BA", "BIH", "070" -> BOSNIA_AND_HERZEGOVINA;
            case "Botswana", "BW", "BWA", "072" -> BOTSWANA;
            case "Bouvet Island", "BV", "BVT", "074" -> BOUVET_ISLAND;
            case "Brazil", "BR", "BRA", "076" -> BRAZIL;
            case "British Indian Ocean Territory (the)", "IO", "IOT", "086" -> BRITISH_INDIAN_OCEAN_TERRITORY;
            case "Brunei Darussalam", "BN", "BRN", "096" -> BRUNEI_DARUSSALAM;
            case "Bulgaria", "BG", "BGR", "100" -> BULGARIA;
            case "Burkina Faso", "BF", "BFA", "854" -> BURKINA_FASO;
            case "Burundi", "BI", "BDI", "108" -> BURUNDI;
            case "Cabo Verde", "CV", "CPV", "132" -> CABO_VERDE;
            case "Cambodia", "KH", "KHM", "116" -> CAMBODIA;
            case "Cameroon", "CM", "CMR", "120" -> CAMEROON;
            case "Canada", "CA", "CAN", "124" -> CANADA;
            case "Cayman Islands (the)", "KY", "CYM", "136" -> CAYMAN_ISLANDS;
            case "Central African Republic (the)", "CF", "CAF", "140" -> CENTRAL_AFRICAN_REPUBLIC;
            case "Chad", "TD", "TCD", "148" -> CHAD;
            case "Chile", "CL", "CHL", "152" -> CHILE;
            case "China", "CN", "CHN", "156" -> CHINA;
            case "Christmas Island", "CX", "CXR", "162" -> CHRISTMAS_ISLAND;
            case "Cocos (Keeling) Islands (the)", "CC", "CCK", "166" -> COCOS_KEELING_ISLANDS;
            case "Colombia", "CO", "COL", "170" -> COLOMBIA;
            case "Comoros (the)", "KM", "COM", "174" -> COMOROS;
            case "Congo (the Democratic Republic of the)", "CD", "COD", "180" -> CONGO_DEMOCRATIC_REPUBLIC;
            case "Congo (the)", "CG", "COG", "178" -> CONGO;
            case "Cook Islands (the)", "CK", "COK", "184" -> COOK_ISLANDS;
            case "Costa Rica", "CR", "CRI", "188" -> COSTA_RICA;
            case "Croatia", "HR", "HRV", "191" -> CROATIA;
            case "Cuba", "CU", "CUB", "192" -> CUBA;
            case "Curaçao", "CW", "CUW", "531" -> CURACAO;
            case "Cyprus", "CY", "CYP", "196" -> CYPRUS;
            case "Czechia", "CZ", "CZE", "203" -> CZECHIA;
            case "Côte d'Ivoire", "CI", "CIV", "384" -> COTE_DIVOIRE;
            case "Denmark", "DK", "DNK", "208" -> DENMARK;
            case "Djibouti", "DJ", "DJI", "262" -> DJIBOUTI;
            case "Dominica", "DM", "DMA", "212" -> DOMINICA;
            case "Dominican Republic (the)", "DO", "DOM", "214" -> DOMINICAN_REPUBLIC;
            case "Ecuador", "EC", "ECU", "218" -> ECUADOR;
            case "Egypt", "EG", "EGY", "818" -> EGYPT;
            case "El Salvador", "SV", "SLV", "222" -> EL_SALVADOR;
            case "Equatorial Guinea", "GQ", "GNQ", "226" -> EQUATORIAL_GUINEA;
            case "Eritrea", "ER", "ERI", "232" -> ERITREA;
            case "Estonia", "EE", "EST", "233" -> ESTONIA;
            case "Eswatini", "SZ", "SWZ", "748" -> ESWATINI;
            case "Ethiopia", "ET", "ETH", "231" -> ETHIOPIA;
            case "Falkland Islands (the) [Malvinas]", "FK", "FLK", "238" -> FALKLAND_ISLANDS;
            case "Faroe Islands (the)", "FO", "FRO", "234" -> FAROE_ISLANDS;
            case "Fiji", "FJ", "FJI", "242" -> FIJI;
            case "Finland", "FI", "FIN", "246" -> FINLAND;
            case "France", "FR", "FRA", "250" -> FRANCE;
            case "French Guiana", "GF", "GUF", "254" -> FRENCH_GUIANA;
            case "French Polynesia", "PF", "PYF", "258" -> FRENCH_POLYNESIA;
            case "French Southern Territories (the)", "TF", "ATF", "260" -> FRENCH_SOUTHERN_TERRITORIES;
            case "Gabon", "GA", "GAB", "266" -> GABON;
            case "Gambia (the)", "GM", "GMB", "270" -> GAMBIA;
            case "Georgia", "GE", "GEO", "268" -> GEORGIA;
            case "Germany", "DE", "DEU", "276" -> GERMANY;
            case "Ghana", "GH", "GHA", "288" -> GHANA;
            case "Gibraltar", "GI", "GIB", "292" -> GIBRALTAR;
            case "Greece", "GR", "GRC", "300" -> GREECE;
            case "Greenland", "GL", "GRL", "304" -> GREENLAND;
            case "Grenada", "GD", "GRD", "308" -> GRENADA;
            case "Guadeloupe", "GP", "GLP", "312" -> GUADELOUPE;
            case "Guam", "GU", "GUM", "316" -> GUAM;
            case "Guatemala", "GT", "GTM", "320" -> GUATEMALA;
            case "Guernsey", "GG", "GGY", "831" -> GUERNSEY;
            case "Guinea", "GN", "GIN", "324" -> GUINEA;
            case "Guinea-Bissau", "GW", "GNB", "624" -> GUINEA_BISSAU;
            case "Guyana", "GY", "GUY", "328" -> GUYANA;
            case "Haiti", "HT", "HTI", "332" -> HAITI;
            case "Heard Island and McDonald Islands", "HM", "HMD", "334" -> HEARD_ISLAND_AND_MCDONALD_ISLANDS;
            case "Holy See (the)", "VA", "VAT", "336" -> HOLY_SEE;
            case "Honduras", "HN", "HND", "340" -> HONDURAS;
            case "Hong Kong", "HK", "HKG", "344" -> HONG_KONG;
            case "Hungary", "HU", "HUN", "348" -> HUNGARY;
            case "Iceland", "IS", "ISL", "352" -> ICELAND;
            case "India", "IN", "IND", "356" -> INDIA;
            case "Indonesia", "ID", "IDN", "360" -> INDONESIA;
            case "Iran (Islamic Republic of)", "IR", "IRN", "364" -> IRAN;
            case "Iraq", "IQ", "IRQ", "368" -> IRAQ;
            case "Ireland", "IE", "IRL", "372" -> IRELAND;
            case "Isle of Man", "IM", "IMN", "833" -> ISLE_OF_MAN;
            case "Israel", "IL", "ISR", "376" -> ISRAEL;
            case "Italy", "IT", "ITA", "380" -> ITALY;
            case "Jamaica", "JM", "JAM", "388" -> JAMAICA;
            case "Japan", "JP", "JPN", "392" -> JAPAN;
            case "Jersey", "JE", "JEY", "832" -> JERSEY;
            case "Jordan", "JO", "JOR", "400" -> JORDAN;
            case "Kazakhstan", "KZ", "KAZ", "398" -> KAZAKHSTAN;
            case "Kenya", "KE", "KEN", "404" -> KENYA;
            case "Kiribati", "KI", "KIR", "296" -> KIRIBATI;
            case "Korea (the Democratic People's Republic of)", "KP", "PRK", "408" -> KOREA_DEMOCRATIC_PEOPLES_REPUBLIC;
            case "Korea (the Republic of)", "KR", "KOR", "410" -> KOREA_REPUBLIC;
            case "Kuwait", "KW", "KWT", "414" -> KUWAIT;
            case "Kyrgyzstan", "KG", "KGZ", "417" -> KYRGYZSTAN;
            case "Lao People's Democratic Republic (the)", "LA", "LAO", "418" -> LAO_PEOPLES_DEMOCRATIC_REPUBLIC;
            case "Latvia", "LV", "LVA", "428" -> LATVIA;
            case "Lebanon", "LB", "LBN", "422" -> LEBANON;
            case "Lesotho", "LS", "LSO", "426" -> LESOTHO;
            case "Liberia", "LR", "LBR", "430" -> LIBERIA;
            case "Libya", "LY", "LBY", "434" -> LIBYA;
            case "Liechtenstein", "LI", "LIE", "438" -> LIECHTENSTEIN;
            case "Lithuania", "LT", "LTU", "440" -> LITHUANIA;
            case "Luxembourg", "LU", "LUX", "442" -> LUXEMBOURG;
            case "Macao", "MO", "MAC", "446" -> MACAO;
            case "Madagascar", "MG", "MDG", "450" -> MADAGASCAR;
            case "Malawi", "MW", "MWI", "454" -> MALAWI;
            case "Malaysia", "MY", "MYS", "458" -> MALAYSIA;
            case "Maldives", "MV", "MDV", "462" -> MALDIVES;
            case "Mali", "ML", "MLI", "466" -> MALI;
            case "Malta", "MT", "MLT", "470" -> MALTA;
            case "Marshall Islands (the)", "MH", "MHL", "584" -> MARSHALL_ISLANDS;
            case "Martinique", "MQ", "MTQ", "474" -> MARTINIQUE;
            case "Mauritania", "MR", "MRT", "478" -> MAURITANIA;
            case "Mauritius", "MU", "MUS", "480" -> MAURITIUS;
            case "Mayotte", "YT", "MYT", "175" -> MAYOTTE;
            case "Mexico", "MX", "MEX", "484" -> MEXICO;
            case "Micronesia (Federated States of)", "FM", "FSM", "583" -> MICRONESIA;
            case "Moldova (the Republic of)", "MD", "MDA", "498" -> MOLDOVA;
            case "Monaco", "MC", "MCO", "492" -> MONACO;
            case "Mongolia", "MN", "MNG", "496" -> MONGOLIA;
            case "Montenegro", "ME", "MNE", "499" -> MONTENEGRO;
            case "Montserrat", "MS", "MSR", "500" -> MONTSERRAT;
            case "Morocco", "MA", "MAR", "504" -> MOROCCO;
            case "Mozambique", "MZ", "MOZ", "508" -> MOZAMBIQUE;
            case "Myanmar", "MM", "MMR", "104" -> MYANMAR;
            case "Namibia", "NA", "NAM", "516" -> NAMIBIA;
            case "Nauru", "NR", "NRU", "520" -> NAURU;
            case "Nepal", "NP", "NPL", "524" -> NEPAL;
            case "Netherlands (the)", "NL", "NLD", "528" -> NETHERLANDS;
            case "New Caledonia", "NC", "NCL", "540" -> NEW_CALEDONIA;
            case "New Zealand", "NZ", "NZL", "554" -> NEW_ZEALAND;
            case "Nicaragua", "NI", "NIC", "558" -> NICARAGUA;
            case "Niger (the)", "NE", "NER", "562" -> NIGER;
            case "Nigeria", "NG", "NGA", "566" -> NIGERIA;
            case "Niue", "NU", "NIU", "570" -> NIUE;
            case "Norfolk Island", "NF", "NFK", "574" -> NORFOLK_ISLAND;
            case "Northern Mariana Islands (the)", "MP", "MNP", "580" -> NORTHERN_MARIANA_ISLANDS;
            case "Norway", "NO", "NOR", "578" -> NORWAY;
            case "Oman", "OM", "OMN", "512" -> OMAN;
            case "Paraguay", "PY", "PRY", "600" -> PARAGUAY;
            case "Peru", "PE", "PER", "604" -> PERU;
            case "Philippines (the)", "PH", "PHL", "608" -> PHILIPPINES;
            case "Pitcairn", "PN", "PCN", "612" -> PITCAIRN;
            case "Poland", "PL", "POL", "616" -> POLAND;
            case "Portugal", "PT", "PRT", "620" -> PORTUGAL;
            case "Puerto Rico", "PR", "PRI", "630" -> PUERTO_RICO;
            case "Qatar", "QA", "QAT", "634" -> QATAR;
            case "Republic of North Macedonia", "MK", "MKD", "807" -> REPUBLIC_OF_NORTH_MACEDONIA;
            case "Romania", "RO", "ROU", "642" -> ROMANIA;
            case "Russian Federation (the)", "RU", "RUS", "643" -> RUSSIAN_FEDERATION;
            case "Rwanda", "RW", "RWA", "646" -> RWANDA;
            case "Réunion", "RE", "REU", "638" -> REUNION;
            case "Saint Barthélemy", "BL", "BLM", "652" -> SAINT_BARTHELEMY;
            case "Saint Helena, Ascension and Tristan da Cunha", "SH", "SHN", "654" -> SAINT_HELENA_ASCENSION_AND_TRISTAN_DA_CUNHA;
            case "Saint Kitts and Nevis", "KN", "KNA", "659" -> SAINT_KITTS_AND_NEVIS;
            case "Saint Lucia", "LC", "LCA", "662" -> SAINT_LUCIA;
            case "Saint Martin (French part)", "MF", "MAF", "663" -> SAINT_MARTIN;
            case "Saint Pierre and Miquelon", "PM", "SPM", "666" -> SAINT_PIERRE_AND_MIQUELON;
            case "Saint Vincent and the Grenadines", "VC", "VCT", "670" -> SAINT_VINCENT_AND_THE_GRENADINES;
            case "Samoa", "WS", "WSM", "882" -> SAMOA;
            case "San Marino", "SM", "SMR", "674" -> SAN_MARINO;
            case "Sao Tome and Principe", "ST", "STP", "678" -> SAO_TOME_AND_PRINCIPE;
            case "Saudi Arabia", "SA", "SAU", "682" -> SAUDI_ARABIA;
            case "Senegal", "SN", "SEN", "686" -> SENEGAL;
            case "Serbia", "RS", "SRB", "688" -> SERBIA;
            case "Seychelles", "SC", "SYC", "690" -> SEYCHELLES;
            case "Sierra Leone", "SL", "SLE", "694" -> SIERRA_LEONE;
            case "Singapore", "SG", "SGP", "702" -> SINGAPORE;
            case "Sint Maarten (Dutch part)", "SX", "SXM", "534" -> SINT_MAARTEN;
            case "Slovenia", "SI", "SVN", "705" -> SLOVENIA;
            case "Solomon Islands", "SB", "SLB", "090" -> SOLOMON_ISLANDS;
            case "Somalia", "SO", "SOM", "706" -> SOMALIA;
            case "South Africa", "ZA", "ZAF", "710" -> SOUTH_AFRICA;
            case "South Georgia and the South Sandwich Islands", "GS", "SGS", "239" -> SOUTH_GEORGIA_AND_THE_SOUTH_SANDWICH_ISLANDS;
            case "South Sudan", "SS", "SSD", "728" -> SOUTH_SUDAN;
            case "Spain", "ES", "ESP", "724" -> SPAIN;
            case "Sri Lanka", "LK", "LKA", "144" -> SRI_LANKA;
            case "Sudan (the)", "SD", "SDN", "729" -> SUDAN;
            case "Suriname", "SR", "SUR", "740" -> SURINAME;
            case "Svalbard and Jan Mayen", "SJ", "SJM", "744" -> SVALBARD_AND_JAN_MAYEN;
            case "Sweden", "SE", "SWE", "752" -> SWEDEN;
            case "Switzerland", "CH", "CHE", "756" -> SWITZERLAND;
            case "Syrian Arab Republic", "SY", "SYR", "760" -> SYRIAN_ARAB_REPUBLIC;
            case "Taiwan (Province of China)", "TW", "TWN", "158" -> TAIWAN;
            case "Tajikistan", "TJ", "TJK", "762" -> TAJIKISTAN;
            case "Tanzania, United Republic of", "TZ", "TZA", "834" -> TANZANIA;
            case "Thailand", "TH", "THA", "764" -> THAILAND;
            case "Timor-Leste", "TL", "TLS", "626" -> TIMOR_LESTE;
            case "Togo", "TG", "TGO", "768" -> TOGO;
            case "Tokelau", "TK", "TKL", "772" -> TOKELAU;
            case "Tonga", "TO", "TON", "776" -> TONGA;
            case "Trinidad and Tobago", "TT", "TTO", "780" -> TRINIDAD_AND_TOBAGO;
            case "Tunisia", "TN", "TUN", "788" -> TUNISIA;
            case "Turkey", "TR", "TUR", "792" -> TURKEY;
            case "Turkmenistan", "TM", "TKM", "795" -> TURKMENISTAN;
            case "Turks and Caicos Islands (the)", "TC", "TCA", "796" -> TURKS_AND_CAICOS_ISLANDS;
            case "Tuvalu", "TV", "TUV", "798" -> TUVALU;
            case "Uganda", "UG", "UGA", "800" -> UGANDA;
            case "Ukraine", "UA", "UKR", "804" -> UKRAINE;
            case "United Arab Emirates (the)", "AE", "ARE", "784" -> UNITED_ARAB_EMIRATES;
            case "United Kingdom of Great Britain and Northern Ireland (the)", "GB", "GBR", "826" -> UNITED_KINGDOM;
            case "United States Minor Outlying Islands (the)", "UM", "UMI", "581" -> UNITED_STATES_MINOR_OUTLYING_ISLANDS;
            case "United States of America (the)", "US", "USA", "840" -> UNITED_STATES_OF_AMERICA;
            case "Uruguay", "UY", "URY", "858" -> URUGUAY;
            case "Uzbekistan", "UZ", "UZB", "860" -> UZBEKISTAN;
            case "Vanuatu", "VU", "VUT", "548" -> VANUATU;
            case "Venezuela (Bolivarian Republic of)", "VE", "VEN", "862" -> VENEZUELA;
            case "Viet Nam", "VN", "VNM", "704" -> VIET_NAM;
            case "Virgin Islands (British)", "VG", "VGB", "092" -> VIRGIN_ISLANDS_BRITISH;
            case "Virgin Islands (U.S.)", "VI", "VIR", "850" -> VIRGIN_ISLANDS_US;
            case "Wallis and Futuna", "WF", "WLF", "876" -> WALLIS_AND_FUTUNA;
            case "Western Sahara", "EH", "ESH", "732" -> WESTERN_SAHARA;
            case "Yemen", "YE", "YEM", "887" -> YEMEN;
            case "Zambia", "ZM", "ZMB", "894" -> ZAMBIA;
            case "Zimbabwe", "ZW", "ZWE", "716" -> ZIMBABWE;
            case "Åland Islands", "AX", "ALA", "248" -> ALAND_ISLANDS;
            default -> EMPTY;
        };
    }
}

