# templates/_helpers.tpl
{{- define "users-service.fullname" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

{{- define "users-service.name" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

{{/* Common labels */}}
{{- define "users-service.labels" -}}
app: {{ include "users-service.name" . }}
environment: {{ .Values.environment }}
app.kubernetes.io/name: {{ include "users-service.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/* Selector labels */}}
{{- define "users-service.selectorLabels" -}}
app: {{ include "users-service.name" . }}
{{- end }}
