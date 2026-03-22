# templates/_helpers.tpl
{{- define "notification-service.fullname" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

{{- define "notification-service.name" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

{{/* Common labels */}}
{{- define "notification-service.labels" -}}
app: {{ include "notification-service.name" . }}
environment: {{ .Values.environment }}
app.kubernetes.io/name: {{ include "notification-service.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/* Selector labels */}}
{{- define "notification-service.selectorLabels" -}}
app: {{ include "notification-service.name" . }}
{{- end }}
