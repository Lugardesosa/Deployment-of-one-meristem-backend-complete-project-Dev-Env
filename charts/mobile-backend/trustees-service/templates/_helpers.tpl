# templates/_helpers.tpl

{{- define "trustees-service.fullname" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

{{- define "trustees-service.name" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

{{/* Common labels */}}
{{- define "trustees-service.labels" -}}
app: {{ include "trustees-service.name" . }}
environment: {{ .Values.environment }}
app.kubernetes.io/name: {{ include "trustees-service.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/* Selector labels */}}
{{- define "trustees-service.selectorLabels" -}}
app: {{ include "trustees-service.name" . }}
{{- end }}
