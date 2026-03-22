{{- define "report-service.fullname" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

{{- define "report-service.name" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

{{/* Common labels */}}
{{- define "report-service.labels" -}}
app: {{ include "report-service.name" . }}
environment: {{ .Values.environment }}
app.kubernetes.io/name: {{ include "report-service.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/* Selector labels */}}
{{- define "report-service.selectorLabels" -}}
app: {{ include "report-service.name" . }}
{{- end }}
