# templates/_helpers.tpl
{{/* Generate a name using the release name */}}
{{- define "users-service.fullname" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

{{- define "users-service.name" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

