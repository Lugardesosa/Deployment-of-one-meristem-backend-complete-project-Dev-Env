# templates/_helpers.tpl

{{- define "wallet-service.fullname" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

{{- define "wallet-service.name" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

{{/* Common labels */}}
{{- define "wallet-service.labels" -}}
app: {{ include "wallet-service.name" . }}
environment: {{ .Values.environment }}
app.kubernetes.io/name: {{ include "wallet-service.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/* Selector labels */}}
{{- define "wallet-service.selectorLabels" -}}
app: {{ include "wallet-service.name" . }}
{{- end }}
