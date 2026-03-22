{{- define "wealth-service.fullname" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

{{- define "wealth-service.name" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

{{/* Common labels */}}
{{- define "wealth-service.labels" -}}
app: {{ include "wealth-service.name" . }}
environment: {{ .Values.environment }}
app.kubernetes.io/name: {{ include "wealth-service.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/* Selector labels */}}
{{- define "wealth-service.selectorLabels" -}}
app: {{ include "wealth-service.name" . }}
{{- end }}
